package view;


import controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.framework.Trace;
import simu.framework.Trace.Level;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicReference;


public class SimulatorGUI extends Application implements ISimulatorUI {

    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IControllerForV controller;
    // Käyttöliittymäkomponentit:
    private Label result;
    private Label resultLabel;
    private Button startButton;
    private Button slowdownButton;
    private Button speedupButton;
    private InputElement timeInput, delayInput;
    private Visualization screen;

    @Override
    public void init() {

        Trace.setTraceLevel(Level.ERR);

        controller = new Controller(this);
    }

    @Override
    public void start(Stage primaryStage) {
        // Käyttöliittymän rakentaminen
        try {

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            primaryStage.setTitle("Simulaattori");

            startButton = new Button();
            startButton.setText("Käynnistä simulointi");
            startButton.setOnAction(event -> {
                controller.startSimulator();
                startButton.setDisable(true);
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

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylÃ¤, oikea, ala, vasen
            hBox.setSpacing(10);   // noodien välimatka 10 pikseliä

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setVgap(10);
            grid.setHgap(5);

            for (int y = 0; y < inputArray.length; y++) {
                grid.add(inputArray[y].getLabel(), 0, y);   // sarake, rivi
                grid.add(inputArray[y].getTextField(), 1, y);          // sarake, rivi
            }

            grid.add(resultLabel, 0, 2);      // sarake, rivi
            grid.add(result, 1, 2);           // sarake, rivi
            grid.add(startButton, 0, 3);  // sarake, rivi
            grid.add(speedupButton, 0, 4);   // sarake, rivi
            grid.add(slowdownButton, 1, 4);   // sarake, rivi

            screen = new Visualization(1000, 800);
//            screen.setOnMouseMoved(event -> {
////                System.out.println(event.getButton());
//                System.out.println("X: " + event.getX() + " Y: " + event.getY());
////                screen.setMouseX(event.getX());
////                screen.setMouseY(event.getY());
//            });

            setCanvasDrag(screen);
            setCanvasZoom(screen);

            hBox.getChildren().addAll(grid, (Canvas) screen);

            Scene scene = new Scene(hBox);
            primaryStage.setScene(scene);
            primaryStage.show();

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    screen.render();
                }
            };

            timer.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void enableStartButton() {
        startButton.setDisable(false);
    }

    private void setCanvasDrag(Canvas canvas) {
        AtomicReference<Double> startX = new AtomicReference<>((double) 0);
        AtomicReference<Double> startY = new AtomicReference<>((double) 0);
        AtomicReference<Double> canvasX = new AtomicReference<>(((Visualization) canvas).getX());
        AtomicReference<Double> canvasY = new AtomicReference<>(((Visualization) canvas).getY());
        final Visualization screen = (Visualization) canvas;

        canvas.setOnMousePressed(event -> {
            startX.set(event.getX());
            startY.set(event.getY());
            canvasX.set(screen.getX());
            canvasY.set(screen.getY());
        });

        canvas.setOnMouseDragged(event -> {
            double deltaX = event.getX() - startX.get();
            double deltaY = event.getY() - startY.get();
            screen.setX(canvasX.get() + deltaX);
            screen.setY(canvasY.get() + deltaY);
        });
    }

    private void setCanvasZoom(Canvas canvas) {
        final Visualization screen = (Visualization) canvas;
        final double minScale = .1, maxScale = 150.0;
        canvas.setOnScroll(event -> {
            double zoomLevel = screen.getZoomLevel();
            if (event.getDeltaY() < 0) screen.setZoomLevel(Math.max(Math.pow(screen.getZoomLevel(), 0.9) - .1, minScale));
            else screen.setZoomLevel(Math.min(Math.pow(screen.getZoomLevel(), 1.15) + .1, maxScale));

            double scale = screen.getZoomLevel() / zoomLevel;
            double deltaX = (event.getX() * scale) - event.getX();
            double deltaY = (event.getY() * scale) - event.getY();

            screen.setX(screen.getX() * scale - deltaX);
            screen.setY(screen.getY() * scale - deltaY);
        });
    }
}