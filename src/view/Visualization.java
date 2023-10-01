package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simu.model.ServicePoint;

import java.util.ArrayList;
import java.util.List;

public class Visualization extends Canvas implements IVisualizationForV, IVisualizationForM {

    private final GraphicsContext gc;
    double x = 0, y = 10;
    private List<ServicePoint> servicePoints = new ArrayList<>();

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
        servicePoints.forEach(servicePoint -> {
            gc.setFill(Color.web("#000000"));
            gc.fillRect(servicePoint.getX(), servicePoint.getY(), 10, 10);
        });
    }

}
