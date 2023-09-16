package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

public class Crosswalk extends ServicePoint{

    private ArrivalProcess crosswalk;
    private boolean crossable = true;

    public Crosswalk(ContinuousGenerator crossingTimeGenerator, ContinuousGenerator crossingFrequencyGenerator, EventList eventList, EventType tyyppi) {
        super(crossingTimeGenerator, eventList, tyyppi);
        crosswalk = new ArrivalProcess(crossingFrequencyGenerator, eventList, EventType.ROAD_CROSSING);
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
