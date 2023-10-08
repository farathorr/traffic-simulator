package controller;

import simu.framework.Engine;

public interface IControllerForV {

    // Rajapinta, joka tarjotaan  käyttöliittymälle:

    public void startSimulator();
    public void speedup();
    public void slowdown();

    public Engine getEngine();
}
