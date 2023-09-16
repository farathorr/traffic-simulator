package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

public class Crosswalk extends ServicePoint{

    private ArrivalProcess crosswalk;

    public Crosswalk(ContinuousGenerator generator, EventList eventList, EventType tyyppi) {
        super(generator, eventList, tyyppi);
        crosswalk = new ArrivalProcess(new Normal(10,5), eventList, EventType.ROAD_CROSSING);
        crosswalk.generateNext();
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        double serviceTime = generator.sample();
        Trace.out(Trace.Level.INFO, "Jalankulkijat ylittävät tietä, aikaa kuluu: " + serviceTime);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }

    public void generateNextEvent() {
        crosswalk.generateNext();
    }




}
