package controller;

import simu.framework.Engine;
import simu.model.Customer;
import simu.model.Level;

public interface IControllerForM {

    // Rajapinta, joka tarjotaan moottorille:

    public void showEndtime(double aika);

    public void render(Level level, String type, double x, double y, String rotation);

    public void enableStartButton();

    public void addCustomerToRendererQueue(Customer customer);

    public void uploadResults(Level level);

    public Engine getEngine();

    public void setToCurrentLevel(Level level);
}
