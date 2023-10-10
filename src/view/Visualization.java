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
    private final boolean DEBUG = true;
    double x = 0, y = 0;
    private final int width, height;
    private final int gridSize = 128;
    private double zoomLevel = 1.0;
    private List<ServicePoint> servicePoints = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private Image roundaboutTurn = new Image("roundabout.png");
    private Image roundaboutRoad = new Image("roundabout-with-road.png");
    private Image roundaboutDouble = new Image("roundabout-double.png");
    private Image roundaboutRoad2 = new Image("roundabout-with-road2.png");
    private Image trafficLightGreen = new Image("trafficlight-green.png");
    private Image trafficLightGreen2 = new Image("trafficlight-green2.png");
    private Image trafficLightRed = new Image("trafficlight-red.png");
    private Image trafficLightRed2 = new Image("trafficlight-red2.png");
    private Image crosswalkImage = new Image("crosswalk.png");
    private Image crosswalkImage2 = new Image("crosswalk2.png");
    private Image crosswalkCrossingImage = new Image("crosswalkCrossing.png");
    private Image crosswalk2CrossingImage = new Image("crosswalk2Crossing.png");
    private Image roadImage = new Image("road.png");
    private Image roadImage2 = new Image("road2.png");
    private Image roadTurn = new Image("road-turn.png");
    private Image tIntersection = new Image("t-intersection.png");
    private Image tIntersection2 = new Image("t-intersection2.png");
    private Image goal = new Image("goal.png");


    public Visualization(int w, int h) {
        super(w, h);
        this.width = w;
        this.height = h;
        gc = this.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        clearScreen();
    }

    public void reset() {
        servicePoints.clear();
        customers.clear();
        clearScreen();
    }

    public int getGridSize() {
        return gridSize;
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
            servicePoints.forEach(this::renderServicePoint);

            try {
                customers.forEach(this::drawQueue);
            } catch (ConcurrentModificationException e) {
                System.out.println("Working as intended");
            }

            if(DEBUG) drawGrid();

        });
    }

    public void renderServicePoint(ServicePoint servicePoint) {
        if (servicePoint.getClass() == Road.class) {
            switch (servicePoint.getRotation()) {
                case "right", "left" -> drawImage(roadImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top", "bottom" -> drawImage(roadImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "right-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-turn" -> drawImage(roadTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "t-intersection-right" -> drawImage(tIntersection2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "t-intersection-top" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "t-intersection-left" -> drawImage(tIntersection2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "t-intersection-bottom" -> drawImage(tIntersection, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);

            }
        }else if (servicePoint.getClass() == Goal.class) {
            switch(servicePoint.getRotation()){
                case "goal" -> drawImage(goal, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
            }
        }
        else if (servicePoint.getClass() == Crosswalk.class) {
                if (((Crosswalk) servicePoint).isCrossable()) {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(crosswalkImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "bottom" -> drawImage(crosswalkImage2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    }
                } else {
                    switch (servicePoint.getRotation()) {
                        case "right" -> drawImage(crosswalkCrossingImage, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                        case "left" -> drawImage(crosswalkCrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        case "top" -> drawImage(crosswalk2CrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                        case "bottom" -> drawImage(crosswalk2CrossingImage, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                        }
                }
            }


        else if (servicePoint.getClass() == Roundabout.class) {
            switch (servicePoint.getRotation()) {
                case "right" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom" -> drawImage(roundaboutTurn, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "right-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                case "left-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-double" -> drawImage(roundaboutDouble, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize + gridSize, gridSize, -gridSize);
                case "right-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "top-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                case "left-road" -> drawImage(roundaboutRoad, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                case "bottom-road" -> drawImage(roundaboutRoad2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
            }
        }
        else if (servicePoint.getClass() == TrafficLights.class) {
            if(((TrafficLights) servicePoint).isGreenLight()){
                switch (servicePoint.getRotation()) {
                    case "bottom" -> drawImage(trafficLightGreen2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "top" -> drawImage(trafficLightGreen2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                    case "left" -> drawImage(trafficLightGreen, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                    case "right" -> drawImage(trafficLightGreen, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize , gridSize, gridSize);
                }
            }
            else {
                switch (servicePoint.getRotation()) {
                    case "bottom" -> drawImage(trafficLightRed2, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                    case "top" -> drawImage(trafficLightRed2, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize + gridSize, -gridSize, -gridSize);
                    case "left" -> drawImage(trafficLightRed, servicePoint.getX() * gridSize + gridSize, servicePoint.getY() * gridSize, -gridSize, gridSize);
                    case "right" -> drawImage(trafficLightRed, servicePoint.getX() * gridSize, servicePoint.getY() * gridSize, gridSize, gridSize);
                }
            }
        }
    };

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

    public void drawGrid() {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.setFont(gc.getFont().font(16));

        double grid = gridSize * zoomLevel;
        int width = (int)(this.width / grid) + 4;
        int height = (int)(this.height / grid) + 4;

        for(int i = -1; i < width; i+=2) {
            double x = this.x % grid + i * grid;
            double y = this.y % grid - grid;
            gc.strokeRect(x, y, grid, grid * height);
        }
        for(int j = -1; j < height; j+=2) {
            double x = this.x % grid - grid;
            double y = this.y % grid + j * grid;
            gc.strokeRect(x, y, grid * width, grid);
        }

        if (zoomLevel > 0.35) {
            gc.setLineWidth(2.0);
            for(int i = -1; i < width; i++) {
                for(int j = -1; j < height; j++) {
                    double x = this.x % grid + i * grid;
                    double y = this.y % grid + j * grid;
                    gc.setStroke(Color.BLACK);
                    String text = String.format("%d, %d", i - (int)((this.x - this.x % grid) / grid), j - (int)((this.y - this.y % grid) / grid));
                    gc.setStroke(Color.WHITE);
                    gc.strokeText(text, x + 2, y + grid - 2);
                    gc.fillText(text, x + 2, y + grid - 2);
                }
            }
        }
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
