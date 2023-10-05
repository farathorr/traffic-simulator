package view;


import controller.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.framework.Trace;
import simu.framework.Trace.Level;

import java.text.DecimalFormat;


public class SimulatorGUI extends Application implements ISimulatorUI {

    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IControllerForV controller;
    // Käyttöliittymäkomponentit:
    private Label result;
    private Label resultLabel;
    private Button startButton;
    private Button slowdownButton;
    private Button speedupButton;
    private InputElement timeInput, delayInput, carMean, carVariance;
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

            carMean = new InputElement("Keskiarvo","5", "Syötä keskiarvo");

            carVariance = new InputElement("Vaihtelevuus","5","Syötä vaihtelevuus");

            InputElement[] inputArray = {timeInput, delayInput};
            InputElement[] customInputArray = {carMean, carVariance};

            resultLabel = new Label("Kokonaisaika:");
            resultLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            result = new Label();
            result.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            result.setPrefWidth(150);

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylÃ¤, oikea, ala, vasen
            hBox.setSpacing(10);   // noodien välimatka 10 pikseliä

            VBox vBox = new VBox();

            GridPane gridDefault = new GridPane();
            gridDefault.setAlignment(Pos.CENTER);
            gridDefault.setVgap(10);
            gridDefault.setHgap(5);

            for (int y = 0; y < inputArray.length; y++) {
                gridDefault.add(inputArray[y].getLabel(), 0, y);   // sarake, rivi
                gridDefault.add(inputArray[y].getTextField(), 1, y);          // sarake, rivi
            }

            gridDefault.add(resultLabel, 0, 2);      // sarake, rivi
            gridDefault.add(result, 1, 2);           // sarake, rivi
            gridDefault.add(startButton, 0, 3);  // sarake, rivi
            gridDefault.add(speedupButton, 0, 4);   // sarake, rivi
            gridDefault.add(slowdownButton, 1, 4);   // sarake, rivi

            GridPane gridCustom = new GridPane();
            gridCustom.setAlignment(Pos.TOP_CENTER);
            gridCustom.setVgap(10);
            gridCustom.setHgap(5);

            for (int i = 0; i < customInputArray.length; i++) {
                gridCustom.add(customInputArray[i].getLabel(), 0, i);   // sarake, rivi
                gridCustom.add(customInputArray[i].getTextField(), 1, i);          // sarake, rivi
            }

            vBox.getChildren().addAll(gridCustom, gridDefault);
            vBox.setSpacing(500);

            screen = new Visualization(1000, 800);

            // TÃ¤ytetÃ¤Ã¤n boxi:
            hBox.getChildren().addAll(vBox, (Canvas) screen);

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
}