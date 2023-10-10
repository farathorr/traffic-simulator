package controller;

import simu.entity.Results;
import simu.framework.Engine;
import simu.model.LevelSettings;

import java.util.List;

public interface IControllerForV {

    // Rajapinta, joka tarjotaan  käyttöliittymälle:

    public void startSimulator();
    public void speedup();
    public void slowdown();

    public Engine getEngine();

    public void setLevelKey(String levelKey);

    public LevelSettings getLevelSettings();

    public List<Results> getResults();
}
