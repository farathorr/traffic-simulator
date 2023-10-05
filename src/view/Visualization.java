package view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import simu.model.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Visualization extends Canvas implements IVisualizationForV, IVisualizationForM {

    private final GraphicsContext gc;
    double x = 0, y = 0;
    private int gridSize = 128;
    private double zoomLevel = 1.0;
    private List<ServicePoint> servicePoints = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private Image roundaboutTurn = new Image("roundabout.png");
    private Image roundaboutRoad = new Image("roundabout-with-road.png");
    private Image roundaboutRoad2 = new Image("roundabout-with-road2.png");
    private Image trafficLight = new Image("trafficlight.png");
    private Image crosswalkImage = new Image("crosswalk.png");
    private Image crosswalkImage2 = new Image("crosswalk2.png");
    private Image roadImage = new Image("road.png");
    private Image roadImage2 = new Image("road2.png");
    private Image tIntersection = new Image("t-intersection.png");
    private Image tIntersection2 = new Image("t-intersection2.png");

    public Visualization(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearScreen();
    }

    public void reset() {
        servicePoints.clear();
        customers.clear();
        clearScreen();
    }

    public void clearScreen() {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void addToRenderQueue(ServicePoint servicePoint) {
        servicePoints.add(servicePoint);
    }

    public void addToCustomerQueue(Customer customer) {
        customers.add(customer);
    }

    public void render() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            servicePoints.forEach(servicePoint -> {
                if (servicePoint.getClass() == Road.class) {
                    switch (servicePoint.getRotation()) {
                        case "right", "left" -> drawImage(roadImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top", "bottom" -> drawImage(roadImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-right" -> drawImage(tIntersection2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "t-intersection-top" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "t-intersection-left" -> drawImage(tIntersection2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-bottom" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } 
                else if (servicePoint.getClass() == Crosswalk.class) {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "bottom" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } 
                else if (servicePoint.getClass() == Roundabout.class) {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                        case "bottom" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "right-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "left-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                        case "bottom-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                    }
                } 
                else if (servicePoint.getClass() == TrafficLights.class) {
                    switch (servicePoint.getRotation()) {
                        case "bottom" -> drawImage(trafficLight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                }
            });

            try {
                customers.forEach(this::drawQueue);
            } catch (ConcurrentModificationException e) {
                System.out.println("Working as intended");
            }
        });
    }

    public void drawQueue(Customer customer) {
        if (customer != null) {
            customer.moveCustomer();
            if (customer.isFirstInQueue()) gc.setFill(Color.web("#32a852"));
            else gc.setFill(Color.web("#eb4034"));

            gc.fillOval(x + (customer.getX() * gridSize + gridSize / 2) * zoomLevel, y + (customer.getY() * gridSize + gridSize / 2) * zoomLevel, 10 * zoomLevel, 10 * zoomLevel);
        }
    }

    public void drawImage(Image img, double x, double y, double w, double h) {
        gc.drawImage(img, this.x + x * zoomLevel, this.y + y * zoomLevel, w * zoomLevel, h * zoomLevel);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
    }
}
