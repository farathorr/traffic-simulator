package controller;

import simu.framework.Engine;
import simu.model.LevelSettings;

public interface IControllerForV {

    // Rajapinta, joka tarjotaan  käyttöliittymälle:

    public void startSimulator();
    public void speedup();
    public void slowdown();

    public Engine getEngine();

    public void setLevelKey(String levelKey);

    public LevelSettings getLevelSettings();
}
