package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualization extends Canvas implements IVisualization {

    private final GraphicsContext gc;
    double x = 0, y = 10;

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

}
