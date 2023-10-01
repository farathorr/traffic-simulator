package view;

public class RenderLoop extends Thread {
    SimulatorGUI ui;

    public RenderLoop(SimulatorGUI ui) {
        this.ui = ui;
    }

    public void run() {
        while(ui.getController().getEngine().simulating()) {
            try {
                Thread.sleep(16);
                ui.getVisualization().render();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
