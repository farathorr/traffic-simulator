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
    double x = 0, y = 10;
    private int gridSize = 128;
    private List<ServicePoint> servicePoints = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private Image roundaboutBottom = new Image("roundabout-bottom.png");
    private Image roundaboutRight = new Image("roundabout-right.png");
    private Image roundaboutTop = new Image("roundabout-top.png");
    private Image roundaboutLeft = new Image("roundabout-left.png");
    private Image roundaboutBottomRoad = new Image("roundabout-bottom-with-road.png");
    private Image roundaboutRightRoad = new Image("roundabout-right-with-road.png");
    private Image roundaboutTopRoad = new Image("roundabout-top-with-road.png");
    private Image roundaboutLeftRoad = new Image("roundabout-left-with-road.png");
    private Image trafficLight = new Image("trafficlight.png");

    private Image crosswalkImageRight = new Image("crosswalk-right.png");
    private Image crosswalkImageTop = new Image("crosswalk-top.png");
    private Image crosswalkImageLeft = new Image("crosswalk-left.png");
    private Image crosswalkImageBottom = new Image("crosswalk-bottom.png");
    private Image roadImageHorizontal = new Image("road.png");
    private Image roadImageVertical = new Image("roadVertical.png");
    private Image tIntersectionRight = new Image("t-intersection-right.png");
    private Image tIntersectionTop = new Image("t-intersection-top.png");
    private Image tIntersectionLeft = new Image("t-intersection-left.png");
    private Image tIntersectionBottom = new Image("t-intersection-bottom.png");

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

    public void addToCustomerQueue(Customer customer) {
        customers.add(customer);
    }

    public void render() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, this.getWidth(), this.getHeight());
            servicePoints.forEach(servicePoint -> {
                if (servicePoint.getClass() == Road.class) {
                    switch (servicePoint.getRotation()) {
                        case "right", "left" ->
                                gc.drawImage(roadImageHorizontal, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top", "bottom" ->
                                gc.drawImage(roadImageVertical, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-right" ->
                                gc.drawImage(tIntersectionRight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-top" ->
                                gc.drawImage(tIntersectionTop, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-left" ->
                                gc.drawImage(tIntersectionLeft, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "t-intersection-bottom" ->
                                gc.drawImage(tIntersectionBottom, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } else if (servicePoint.getClass() == Crosswalk.class) {
                    switch (servicePoint.getRotation()) {
                        case "right" ->
                                gc.drawImage(crosswalkImageRight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "left" ->
                                gc.drawImage(crosswalkImageLeft, (servicePoint.getX() - 1) * gridSize, (servicePoint.getY() + 1) * gridSize, -gridSize, -gridSize);
                        case "top" ->
                                gc.drawImage(crosswalkImageTop, (servicePoint.getX() + 1) * gridSize, (servicePoint.getY() + 1) * gridSize, -gridSize, -gridSize);
                        case "bottom" ->
                                gc.drawImage(crosswalkImageBottom, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } else if (servicePoint.getClass() == Roundabout.class) {
                    switch (servicePoint.getRotation()) {
                        case "right" ->
                                gc.drawImage(roundaboutRight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" ->
                                gc.drawImage(roundaboutTop, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "left" ->
                                gc.drawImage(roundaboutLeft, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "bottom" ->
                                gc.drawImage(roundaboutBottom, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "right-road" ->
                                gc.drawImage(roundaboutRightRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top-road" ->
                                gc.drawImage(roundaboutTopRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "left-road" ->
                                gc.drawImage(roundaboutLeftRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "bottom-road" ->
                                gc.drawImage(roundaboutBottomRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } else if (servicePoint.getClass() == TrafficLights.class) {
                    switch (servicePoint.getRotation()) {
                        case "bottom" ->
                                gc.drawImage(trafficLight, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
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

            gc.fillOval(customer.getX() * gridSize + gridSize / 2, customer.getY() * gridSize + gridSize / 2, 10, 10);
        }
    }

}
