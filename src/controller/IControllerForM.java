package controller;

import simu.model.Level;

public interface IControllerForM {

    // Rajapinta, joka tarjotaan moottorille:

    public void showEndtime(double aika);

    public void render(Level level, String type, double x, double y, double rotation);
}
