package view;


import controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import simu.entity.Level_variables;
import simu.entity.Results;
import simu.framework.Trace;
import simu.model.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS;
import static javafx.scene.control.TableView.UNCONSTRAINED_RESIZE_POLICY;


public class SimulatorGUI extends Application implements ISimulatorUI {

    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IControllerForV controller;
    // Käyttöliittymäkomponentit:
    private Label result;
    private Label resultLabel;
    private Button startButton;
    private ComboBox<String> levelComboBox;
    private Button slowdownButton;
    private Button speedupButton;
    private InputElement timeInput, delayInput, sePointMean, sePointVariance, sePointMean2, sePointVariance2;
    private Visualization screen = new Visualization(700, 700);
    private Level selectedLevel;
    private ServicePoint selectedServicePoint;
    ListView<ServicePoint> servicePointListView;
    private LevelSettings levelSettings;
    private String placeTileType = "road", placeRotation = "right";
    private final int[] lastPlaced = {-9999, -9999};

    @Override
    public void init() {

        Trace.setTraceLevel(Trace.Level.ERR);

        controller = new Controller(this);
        levelSettings = controller.getLevelSettings();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setTitle("Simulaattori");

        VBox headerConteiner = new VBox();

        levelComboBox = new ComboBox<>();
        servicePointListView = new ListView<>();
        servicePointListView.setMaxHeight(200);

        sePointMean = new InputElement("Vihreä valo", "5", "Arvo", "mean");
        sePointVariance = new InputElement("Keston vaihtelevuus", "5", "Vaihtelevuus", "variance");
        sePointMean2 = new InputElement("Punainen valo", "5", "Arvo", "mean2");
        sePointVariance2 = new InputElement("Keston vaihtelevuus", "5", "Vaihtelevuus", "variance2");

        InputElement[] customInputArray = {sePointMean, sePointVariance, sePointMean2, sePointVariance2};

        servicePointListView.getSelectionModel().selectedItemProperty().addListener((observable, oldSePoint, newSePoint) -> {
            selectedServicePoint = newSePoint;
            if (newSePoint == null) {
                for (InputElement inputElement : customInputArray) inputElement.setVisible(false);
                return;
            }

            @FunctionalInterface
            interface SettingsKeys {
                String search(String key);
            }

            SettingsKeys settings = (String key) -> {
                if (newSePoint.hasSettings(key)) return String.valueOf(newSePoint.getSettings(key));
                else return switch (key) {
                    case "mean" -> String.valueOf(newSePoint.getMean());
                    case "variance" -> String.valueOf(newSePoint.getVariance());
                    case "mean2" -> String.valueOf(newSePoint.getMean2());
                    case "variance2" -> String.valueOf(newSePoint.getVariance2());
                    default -> "";
                };
            };

            for (InputElement inputElement : customInputArray) {
                inputElement.setVisible(true);
                String settingKey = inputElement.getId();

                inputElement.getTextField().setText(settings.search(settingKey));
            }


            if (newSePoint.getClass() == TrafficLights.class) {
                sePointMean.getLabel().setText("Vihreän valon kesto");
                sePointMean2.getLabel().setText("Punaisen valon kesto");
            } else if (newSePoint.getClass() == Crosswalk.class) {
                sePointMean.getLabel().setText("Tien ylityksen kesto");
                sePointMean2.getLabel().setText("Tien ylitystahti");
            }
        });

        for (InputElement inputElement : customInputArray) {
            inputElement.getTextField().setOnKeyTyped(event -> {
                try {
                    double value = Double.parseDouble(inputElement.getTextField().getText());
                    System.out.println(value);
                    selectedServicePoint.setSettings(inputElement.getId(), value);
                } catch (NumberFormatException e) {
                    inputElement.getTextField().setText("");
                }
            });
        }



        levelComboBox.getItems().addAll("DEBUG world", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10");
        levelComboBox.setPromptText("Please Select");
        levelComboBox.setOnAction(event -> {
            String value = levelComboBox.getValue();
            screen.reset();
            screen.setX(0);
            screen.setY(0);
            screen.setZoomLevel(1);
            selectedLevel = controller.getEngine().getLevelController().getLevel(value); // Also renders the level
            controller.setLevelKey(value);
            updateServicePointSettingsList();
        });

        headerConteiner.getChildren().addAll(levelComboBox, servicePointListView);

        startButton = new Button();
        startButton.setText("Käynnistä simulointi");
        startButton.setOnAction(event -> {
            controller.startSimulator();
            startButton.setDisable(true);
            levelComboBox.setDisable(true);
        });

        slowdownButton = new Button();
        slowdownButton.setText("Hidasta");
        slowdownButton.setOnAction(e -> controller.slowdown());

        speedupButton = new Button();
        speedupButton.setText("Nopeuta");
        speedupButton.setOnAction(e -> controller.speedup());

        timeInput = new InputElement("Simulointiaika:", "1000", "Syötä aika");

        delayInput = new InputElement("Viive:", "10", "Syötä viive");

        InputElement[] inputArray = {timeInput, delayInput};

        timeInput.getTextField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    timeInput.getTextField().setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        delayInput.getTextField().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    delayInput.getTextField().setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });

        resultLabel = new Label("Kokonaisaika:");
        result = new Label();
        result.setPrefWidth(150);

        Button helpButton = new Button("Ohjeet");
        helpButton.setOnAction(e -> {
            Stage helpWindow = new Stage();
            helpWindow.setTitle("Ohjeet");
            Label helpLabel = new Label();
            helpLabel.setText("""
                    Valitse simulaattorin vasemmasta yläkulmasta haluamasi taso.
                    Tasovalikon alle syntyy lista tason eri palvelupisteistä, joiden arvoja voit muuttaa palvelupistelistan alla olevista kentistä.
                    Simulointiaika-kentästä voit muuttaa simulaation kokonaisaikaa.
                    Viive-kentästä voit muuttaa simulaation visuaalisen esityksen nopeutta.
                    Nopeuta- ja hidasta-napeilla voit vaihtaa simulaation nopeutta sen pyöriessä.
                    Kun olet valinnut haluamasi tason ja vaihtanut haluamasi arvoja, voit käynnistää simulaation Käynnistä simulaatio-napista.
                    Tulokset-napista voit tutkia simulaation tuloksia sen suorituksen jälkeen.
                    """);
            HBox container = new HBox(helpLabel);
            container.setSpacing(10);
            container.setPadding(new Insets(15));
            container.setAlignment(Pos.CENTER);
            Scene scene = new Scene(container);
            scene.getStylesheets().add("styles.css");
            helpWindow.setScene(scene);
            helpWindow.show();
        });

        Button resultsButton = new Button("Tulokset");
        resultsButton.setOnAction(e -> {
            Stage resultsWindow = new Stage();
            resultsWindow.setTitle("Tulokset");
            GridPane resultsGrid = new GridPane();
            Label levelsLabel = new Label("Tasot");
            ChoiceBox<String> levelsChoicebox = new ChoiceBox<>();
            VBox levelChoiceAndLabel = new VBox(levelsLabel, levelsChoicebox);

            ArrayList<Level_variables> servicePointsList = new ArrayList<>();
            TableView<Level_variables> variableTable = new TableView<>();
            ArrayList<Level_variables> goalsList = new ArrayList<>();
            TableView<Level_variables> goalTable = new TableView<>();

            List<Results> resultsList = controller.getResults();
            List<String> levels = new ArrayList<>();
            TableView<Results> simulationsTable = new TableView<>();
            variableTable.setPrefWidth(600);
            goalTable.setPrefWidth(500);

            for (Results selectedResult : resultsList) {
                if (!levels.contains(selectedResult.getSimulationLevel())) {
                    levels.add(selectedResult.getSimulationLevel());
                }
            }
            Comparator<String> compareByLevelName = Comparator.comparing(String::toString);
            levels.sort(compareByLevelName);
            levelsChoicebox.getItems().addAll(levels);

            levelsChoicebox.setOnAction(ae -> {
                ArrayList<Results> simulationsList = new ArrayList<>();
                for (Results selectedResult : resultsList) {
                    if (selectedResult.getSimulationLevel().equals(levelsChoicebox.getValue())) {
                        simulationsList.add(selectedResult);
                    }
                }
                simulationsTable.setItems(FXCollections.observableArrayList(simulationsList));

                TableColumn<Results, Integer> simulationIdCol = new TableColumn<>("Simulaation id");
                TableColumn<Results, Double> simulationTimeCol = new TableColumn<>("Simulaation aika");

                simulationIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
                simulationTimeCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSimulationTime()).asObject());

                simulationsTable.getColumns().setAll(simulationIdCol, simulationTimeCol);
            });

            simulationsTable.getSelectionModel().selectedItemProperty().addListener(ae -> {
                if(simulationsTable.getSelectionModel().getSelectedItem() != null) {
                    servicePointsList.clear();
                    goalsList.clear();
                    int selectedSimulation = simulationsTable.getSelectionModel().getSelectedItem().getId();
                    List<Level_variables> list = controller.getLevelVariables();
                    for (Level_variables level_variable : list) {
                        if (level_variable.getLevelId().getId() == selectedSimulation) {
                            if (level_variable.getServicePointName().contains("goal")) goalsList.add(level_variable);
                            else servicePointsList.add(level_variable);
                        }
                    }
                    ObservableList<Level_variables> servicePointsObservableList = FXCollections.observableArrayList(servicePointsList);
                    ObservableList<Level_variables> goalsObservableList = FXCollections.observableArrayList(goalsList);
                    variableTable.setItems(servicePointsObservableList);
                    goalTable.setItems(goalsObservableList);

                    TableColumn<Level_variables, String> servicePointNameCol = new TableColumn<>("Nimi");
                    TableColumn<Level_variables, String> goalNameCol = new TableColumn<>("Nimi");
                    TableColumn<Level_variables, Double> mean1Col = new TableColumn<>("Keskiarvo1");
                    TableColumn<Level_variables, Double> mean2Col = new TableColumn<>("Keskiarvo2");
                    TableColumn<Level_variables, Double> variance1Col = new TableColumn<>("Vaihtelevuus1");
                    TableColumn<Level_variables, Double> variance2Col = new TableColumn<>("Vaihtelevuus2");
                    TableColumn<Level_variables, Integer> goalCarCountCol = new TableColumn<>("Autojen määrä");
                    TableColumn<Level_variables, Double> averageTimeCol = new TableColumn<>("Keskimääräinen aika");

                    servicePointNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServicePointName()));

                    goalNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServicePointName()));
                    mean1Col.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMean1()).asObject());
                    mean2Col.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMean2()).asObject());
                    variance1Col.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getVariance1()).asObject());
                    variance2Col.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getVariance2()).asObject());

                    goalCarCountCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCarCount()).asObject());
                    averageTimeCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAverageTime()).asObject());

                    variableTable.getColumns().setAll(servicePointNameCol, mean1Col, mean2Col, variance1Col, variance2Col);
                    goalTable.getColumns().setAll(goalNameCol, goalCarCountCol, averageTimeCol);

                }});

            variableTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            goalTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            simulationsTable.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            Insets insets = new Insets(15);
            resultsGrid.setHgap(10);
            resultsGrid.setVgap(10);

            if(!levels.isEmpty()){
                levelsChoicebox.setValue(levels.get(0));
            }

            resultsGrid.add(levelChoiceAndLabel, 0, 0);
            resultsGrid.add(simulationsTable, 0, 1, 2, 1);
            resultsGrid.add(variableTable, 0, 2);
            resultsGrid.add(goalTable, 1, 2);
            resultsGrid.setPadding(insets);
            resultsGrid.setAlignment(Pos.CENTER);
            Scene scene = new Scene(resultsGrid);
            scene.getStylesheets().add("styles.css");
            resultsWindow.setScene(scene);
            resultsWindow.show();
        });


        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylÃ¤, oikea, ala, vasen
        hBox.setSpacing(10);   // noodien välimatka 10 pikseliä

        VBox vBox = new VBox();

        GridPane footerGrid = new GridPane();
        footerGrid.setAlignment(Pos.CENTER);
        footerGrid.setVgap(10);
        footerGrid.setHgap(5);

        for (int y = 0; y < inputArray.length; y++) {
            footerGrid.add(inputArray[y].getLabel(), 0, y);   // sarake, rivi
            footerGrid.add(inputArray[y].getTextField(), 1, y);          // sarake, rivi
        }

        footerGrid.add(resultLabel, 0, 2);      // sarake, rivi
        footerGrid.add(result, 1, 2);           // sarake, rivi
        footerGrid.add(startButton, 0, 3);  // sarake, rivi
        footerGrid.add(speedupButton, 0, 4);   // sarake, rivi
        footerGrid.add(slowdownButton, 1, 4);   // sarake, rivi
        footerGrid.add(helpButton, 0, 7);   // sarake, rivi
        footerGrid.add(resultsButton, 1, 7);   // sarake, rivi

        footerGrid.setPadding(new Insets(15));

        GridPane gridCustom = new GridPane();
        gridCustom.setAlignment(Pos.TOP_CENTER);
        gridCustom.setVgap(10);
        gridCustom.setHgap(5);

        for (int i = 0; i < customInputArray.length; i++) {
            customInputArray[i].setVisible(false);
            gridCustom.add(customInputArray[i].getLabel(), 0, i);   // sarake, rivi
            gridCustom.add(customInputArray[i].getTextField(), 1, i);          // sarake, rivi
        }

        vBox.getChildren().addAll(headerConteiner, gridCustom, footerGrid);

        setCanvasDrag(screen);
        setCanvasZoom(screen);
        setCanvasDrawPreview(screen);

        hBox.getChildren().addAll(vBox, (Canvas) screen);

        Scene scene = new Scene(hBox);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            lastPlaced[0] = -9999;
            lastPlaced[1] = -9999;
            switch (event.getCode()) {
                case DIGIT0 -> placeTileType = "air";
                case DIGIT1 -> {
                    switch (placeTileType) {
                        case "road" -> placeTileType = "road-turn";
                        case "road-turn" -> placeTileType = "t-intersection";
                        default -> placeTileType = "road";
                    }
                }
                case DIGIT2 -> {
                    switch (placeTileType) {
                        case "crosswalk" -> placeTileType = "traffic-lights";
                        default -> placeTileType = "crosswalk";
                    }
                }
                case DIGIT3 -> {
                    switch (placeTileType) {
                        case "roundabout-entrance" -> placeTileType = "roundabout";
                        case "roundabout" -> placeTileType = "roundabout-road";
                        case "roundabout-road" -> placeTileType = "roundabout-double";
                        default -> placeTileType = "roundabout-entrance";
                    }
                }
                case DIGIT4 -> {
                    switch (placeTileType) {
                        case "start" -> placeTileType = "goal";
                        default -> placeTileType = "start";
                    }
                }
                case DIGIT5 -> placeTileType = "arrow";
                case R -> {
                    switch (placeRotation) {
                        case "right" -> placeRotation = "bottom";
                        case "bottom" -> placeRotation = "left";
                        case "left" -> placeRotation = "top";
                        case "top" -> placeRotation = "right";
                    }
                }
                case E -> {
                    System.out.println("\n".repeat(50));
                    screen.exportSelectedLevel();
                    System.out.println("\n".repeat(5));
                }
                case Q -> {
                    screen.pickATileInfo();
                    placeRotation = screen.getPlaceRotation();
                    placeTileType = screen.getPlaceTileType();
                }
                case D -> {
                    Debug.getInstance().toggle();
                }
            }

            screen.setPlaceRotation(placeRotation);
            screen.setPlaceTileType(placeTileType);
        });


        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double padding = Math.max(oldVal.doubleValue() - screen.getWidth(), 380);
            double width = Math.max(newVal.doubleValue() - padding, 200.0);
            screen.setWidth((int) width); // Set inside variables
            screen.setWidth(width); // Set canvas size
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double padding = oldVal.doubleValue() - screen.getHeight();
            double height = Math.max(newVal.doubleValue() - padding, 200.0);
            screen.setHeight((int) height); // Set inside variables
            screen.setHeight(height); // Set canvas size
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                screen.render();
            }
        };

        timer.start();
    }

    @Override
    public double getTime() {
        return Double.parseDouble(timeInput.getTextField().getText());
    }

    @Override
    public long getDelay() {
        if(delayInput.getTextField().getText().equals("")) return 0;
        return Long.parseLong(delayInput.getTextField().getText());
    }

    @Override
    public void setEndTime(double aika) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.result.setText(formatter.format(aika));
    }

    @Override
    public Visualization getVisualization() {
        return screen;
    }

    public Controller getController() {
        return (Controller) controller;
    }

    public void enableSimulationSettings() {
        startButton.setDisable(false);
        levelComboBox.setDisable(false);
    }

    private void setCanvasDrag(Canvas canvas) {
        AtomicReference<Double> startX = new AtomicReference<>((double) 0);
        AtomicReference<Double> startY = new AtomicReference<>((double) 0);
        AtomicReference<Double> canvasX = new AtomicReference<>(((Visualization) canvas).getX());
        AtomicReference<Double> canvasY = new AtomicReference<>(((Visualization) canvas).getY());
        final Visualization screen = (Visualization) canvas;

        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                startX.set(event.getX());
                startY.set(event.getY());
                canvasX.set(screen.getX());
                canvasY.set(screen.getY());
            } else if (event.getButton() == MouseButton.SECONDARY) placeTilesOnCanvas(event);
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getX() - startX.get();
                double deltaY = event.getY() - startY.get();
                screen.setX(canvasX.get() + deltaX);
                screen.setY(canvasY.get() + deltaY);
            } else if (event.getButton() == MouseButton.SECONDARY) placeTilesOnCanvas(event);
        });
    }

    private void setCanvasDrawPreview(Canvas canvas) {
        final Visualization screen = (Visualization) canvas;
        canvas.setOnMouseMoved(event -> {
            if(!Debug.getInstance().isDebug()) return;
            double gridSize = screen.getGridSize() * screen.getZoomLevel();
            int scaleX = (int) Math.floor((event.getX() - screen.getX()) / gridSize);
            int scaleY = (int) Math.floor((event.getY() - screen.getY()) / gridSize);
            screen.setPlaceRotation(placeRotation);
            screen.setPlaceTileType(placeTileType);
            screen.setPreviewX(scaleX);
            screen.setPreviewY(scaleY);
        });
    }

    private void placeTilesOnCanvas(MouseEvent event) {
        if (!Debug.getInstance().isDebug()) return;
        double gridSize = screen.getGridSize() * screen.getZoomLevel();
        int scaleX = (int) Math.floor((event.getX() - screen.getX()) / gridSize);
        int scaleY = (int) Math.floor((event.getY() - screen.getY()) / gridSize);

        if (scaleX == lastPlaced[0] && scaleY == lastPlaced[1]) return;
        if (placeTileType.equals("arrow")) {
            screen.createServicePointConnection(scaleX, scaleY, placeRotation);
        } else if (placeTileType.equals("air")) {
            screen.deleteServicePoint(scaleX, scaleY);
        } else screen.createNewServicePoint(scaleX, scaleY, placeTileType, placeRotation);
        lastPlaced[0] = scaleX;
        lastPlaced[1] = scaleY;
    }

    private void setCanvasZoom(Canvas canvas) {
        final Visualization screen = (Visualization) canvas;
        final double minScale = .05, maxScale = 150.0;
        canvas.setOnScroll(event -> {
            double zoomLevel = screen.getZoomLevel();
            if (event.getDeltaY() < 0)
                screen.setZoomLevel(Math.max(Math.pow(screen.getZoomLevel(), 0.9) - .1, minScale));
            else screen.setZoomLevel(Math.min(Math.pow(screen.getZoomLevel(), 1.15) + .1, maxScale));

            double scale = screen.getZoomLevel() / zoomLevel;
            double deltaX = (event.getX() * scale) - event.getX();
            double deltaY = (event.getY() * scale) - event.getY();

            screen.setX(screen.getX() * scale - deltaX);
            screen.setY(screen.getY() * scale - deltaY);
        });
    }

    private void updateServicePointSettingsList() {
        servicePointListView.getItems().clear();
        for (ServicePoint servicePoint : selectedLevel.getServicePoints()) {
            if (servicePoint.getClass() == TrafficLights.class || servicePoint.getClass() == Crosswalk.class) {
                servicePointListView.getItems().add(servicePoint);
            }
        }
    }
}