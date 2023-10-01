package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import simu.model.Intersection;
import simu.model.Roundabout;
import simu.model.ServicePoint;

import java.util.ArrayList;
import java.util.List;

public class Visualization extends Canvas implements IVisualizationForV, IVisualizationForM {

    private final GraphicsContext gc;
    double x = 0, y = 10;
    private List<ServicePoint> servicePoints = new ArrayList<>();
    private Image roundaboutBottom = new Image("roundabout-bottom.png");
    private Image roundaboutRight = new Image("roundabout-right.png");
    private Image roundaboutTop = new Image("roundabout-top.png");
    private Image roundaboutLeft = new Image("roundabout-left.png");

    public Visualization(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearScreen();
    }

    public void clearScreen() {
        gc.setFill(Color.YELLOW);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void newCustomer() {
        gc.setFill(Color.web("#32a852"));
        gc.fillOval(x, y, 10, 10);

        x = (x + 10) % this.getWidth();
        //j = (j + 12) % this.getHeight();
        if (x == 0) y += 10;
    }

    public void addToRenderQueue(ServicePoint servicePoint) {
        servicePoints.add(servicePoint);
    }

    public void render() {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        int gridSize = 128;
        servicePoints.forEach(servicePoint -> {
            if(servicePoint.getClass() == Intersection.class) {
                gc.setFill(Color.web("#000000"));
                gc.fillRect(servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize / 3.0);
            }
            if(servicePoint.getClass() == Roundabout.class) {
                switch (servicePoint.getRotation()) {
                    case "right" -> gc.drawImage(roundaboutRight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "top" -> gc.drawImage(roundaboutTop, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "left" -> gc.drawImage(roundaboutLeft, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "bottom" -> gc.drawImage(roundaboutBottom, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                }
            }
        });
    }

}
