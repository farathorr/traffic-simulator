package view;


import controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
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

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


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
    private InputElement timeInput, delayInput, carMean, carVariance;
    private Visualization screen = new Visualization(1000, 800);
    private Level selectedLevel;
    private ServicePoint selectedServicePoint;
    ListView<ServicePoint> servicePointListView;
    private LevelSettings levelSettings;
    private String placeTileType = "road", placeRotation = "right";
    private final int[] lastPlaced = { -9999, -9999 };

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
        carMean = new InputElement("Keskiarvo", "5", "Syötä keskiarvo");
        carVariance = new InputElement("Vaihtelevuus", "5", "Syötä vaihtelevuus");

        InputElement[] customInputArray = {carMean, carVariance};

        servicePointListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (InputElement inputElement : customInputArray) inputElement.setVisible(true);


            selectedServicePoint = newValue;
        });

        carVariance.getTextField().setOnKeyTyped(event -> {
            String key = selectedLevel.getLevelName() + selectedServicePoint.getScheduledEventType() + "generator2";
            levelSettings.add(key, Double.parseDouble(carVariance.getTextField().getText()));
            System.out.println("Vaihtelevuus: " + levelSettings.get(key));
        });

        levelComboBox.getItems().addAll("DEBUG world", "Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6");
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

        resultLabel = new Label("Kokonaisaika:");
        resultLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        result = new Label();
        result.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
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
            helpWindow.setScene(new Scene(container));
            helpWindow.show();
        });

        Button resultsButton = new Button("Tulokset");
        resultsButton.setOnAction(e -> {
            Stage resultsWindow = new Stage();
            resultsWindow.setTitle("Tulokset");
            GridPane resultsGrid = new GridPane();
            Label levelsLabel = new Label("Tasot");
            Label simulationsLabel = new Label("Simulaatiot");
            List<Results> resultsList = controller.getResults();
            Button findButton = new Button("Etsi tuloksia");
            ChoiceBox<String> levelsChoicebox = new ChoiceBox<>();
            ChoiceBox<Integer> simulationsChoicebox = new ChoiceBox<>();
            ArrayList<Level_variables> variablesList = new ArrayList<>();
            TableView<Level_variables> variableTable = new TableView<>();
            variableTable.setPrefWidth(400);

            List<String> levels = new ArrayList<>();
            for (Results selectedResult : resultsList) {
                if (!levels.contains(selectedResult.getSimulationLevel())) {
                    levels.add(selectedResult.getSimulationLevel());
                }
            }
            levelsChoicebox.getItems().addAll(levels);

            levelsChoicebox.setOnAction(ae -> {
                simulationsChoicebox.getItems().clear();
                for (Results selectedResult : resultsList) {
                    if (selectedResult.getSimulationLevel().equals(levelsChoicebox.getValue())) {
                        simulationsChoicebox.getItems().add(selectedResult.getId());
                    }
                }
            });

            findButton.setOnAction(ae -> {
                System.out.println(simulationsChoicebox.getValue());
                if (!(simulationsChoicebox.getValue() == null)) {
                    int selectedSimulation = simulationsChoicebox.getValue();
                    List<Level_variables> list = controller.getLevelVariables();
                    for (Level_variables level_variable : list) {
                        if (level_variable.getLevelId().getId() == selectedSimulation) {
                            System.out.println(level_variable.getServicePointName());
                            variablesList.add(level_variable);
                        }
                    }
                    ObservableList<Level_variables> variablesObservableList = FXCollections.observableArrayList(variablesList);
                    variableTable.setItems(variablesObservableList);

                    TableColumn<Level_variables, String> servicePointNameCol = new TableColumn<>("Service Point");
                    TableColumn<Level_variables, Double> eventIntervalsCol = new TableColumn<>("Event Interval");
                    TableColumn<Level_variables, Double> leadTimeCol = new TableColumn<>("Lead time");

                    servicePointNameCol.setCellValueFactory( data -> new SimpleStringProperty(data.getValue().getServicePointName()));
                    eventIntervalsCol.setCellValueFactory( data -> new SimpleDoubleProperty(data.getValue().getEventInterval()).asObject());
                    leadTimeCol.setCellValueFactory( data -> new SimpleDoubleProperty(data.getValue().getLeadTime()).asObject());

                    variableTable.getColumns().setAll(servicePointNameCol, eventIntervalsCol, leadTimeCol);
                }
            });


            Insets insets = new Insets(15);
            variableTable.setPadding(insets);

            resultsGrid.add(levelsLabel, 0, 0);
            resultsGrid.add(simulationsLabel, 1, 0);
            resultsGrid.add(levelsChoicebox, 0, 1);
            resultsGrid.add(simulationsChoicebox, 1, 1);
            resultsGrid.add(findButton, 2, 1);
            resultsGrid.add(variableTable, 0, 2);
            resultsGrid.setPadding(insets);
            resultsGrid.setAlignment(Pos.CENTER);
            resultsWindow.setScene(new Scene(resultsGrid));
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
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            switch (event.getCode()) {
                case DIGIT0 -> placeTileType = "road";
                case DIGIT1 -> placeTileType = "roundabout";
                case DIGIT2 -> placeTileType = "trafficlights"; //wtf
                case DIGIT3 -> placeTileType = "crosswalk";
                case DIGIT4 -> placeTileType = "goal";
                case DIGIT5 -> placeTileType = "turn";
                case DIGIT6 -> placeTileType = "t-intersection";
                case DIGIT7 -> placeTileType = "double";
                case DIGIT8 -> placeTileType = "r-road";
                case DIGIT9 -> placeTileType = "arrow";
                case R -> {
                    switch (placeRotation) {
                        case "right" -> placeRotation = "bottom";
                        case "bottom" -> placeRotation = "left";
                        case "left" -> placeRotation = "top";
                        case "top" -> placeRotation = "right";
                    }
                }
                case E -> {
                    screen.exportSelectedLevel();
                }
            }

            screen.setPlaceRotation(placeRotation);
            screen.setPlaceTileType(placeTileType);
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                screen.render();
            }
        };

        timer.start();
    }


    //Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)

    @Override
    public double getTime() {
        return Double.parseDouble(timeInput.getTextField().getText());
    }

    @Override
    public long getDelay() {
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
            } else if(event.getButton() == MouseButton.SECONDARY) placeTilesOnCanvas(event);
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getX() - startX.get();
                double deltaY = event.getY() - startY.get();
                screen.setX(canvasX.get() + deltaX);
                screen.setY(canvasY.get() + deltaY);
            } else if(event.getButton() == MouseButton.SECONDARY) placeTilesOnCanvas(event);
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
        if(!Debug.getInstance().isDebug()) return;
        double gridSize = screen.getGridSize() * screen.getZoomLevel();
        int scaleX = (int) Math.floor((event.getX() - screen.getX()) / gridSize);
        int scaleY = (int) Math.floor((event.getY() - screen.getY()) / gridSize);

        if (scaleX == lastPlaced[0] && scaleY == lastPlaced[1]) return;
        if (placeTileType.equals("arrow")) {
            screen.createServicePointConnection(scaleX, scaleY, placeRotation);
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