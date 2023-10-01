package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

public class Crosswalk extends ServicePoint {
    private ArrivalProcess crosswalk;
    private boolean crossable = true;

    public Crosswalk(ContinuousGenerator crossingTimeGenerator, ContinuousGenerator crossingFrequencyGenerator, EventList eventList, String type) {
        super(crossingTimeGenerator, eventList, type);
        crosswalk = new ArrivalProcess(crossingFrequencyGenerator, eventList, type + " Road Crossing");
        crosswalk.generateNext();
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        double serviceTime = generator.sample();
        Trace.out(Trace.Level.INFO, "Jalankulkijat ylittävät tietä, aikaa kuluu: " + serviceTime);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
        reserved = true;
    }

    public void switchCrossable() {
        crossable = !crossable;
        crosswalk.generateNext();
    }
}
