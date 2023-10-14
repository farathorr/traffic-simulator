package view;

/**
 * ISimulatorUI-rajapinta, joka sisältää metodeja, joita käytetään simulaattorin käyttöliittymässä.
 */
public interface ISimulatorUI {

    public double getTime();

    public long getDelay();

    public void setEndTime(double time);

    public Visualization getVisualization();

    public void enableSimulationSettings();
}