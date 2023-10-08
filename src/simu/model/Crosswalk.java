package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

public class Crosswalk extends ServicePoint {
    private ArrivalProcess crosswalk;
    private boolean crossable = true;
    private Event nextCrossingEvent = null;
    private ContinuousGenerator crossingFrequencyGenerator;

    public Crosswalk(ContinuousGenerator crossingTimeGenerator, ContinuousGenerator crossingFrequencyGenerator, EventList eventList, String type) {
        super(crossingTimeGenerator, eventList, type);
        this.crossingFrequencyGenerator = crossingFrequencyGenerator;
    }

    public void init() {
        crosswalk = new ArrivalProcess(crossingFrequencyGenerator, eventList, this.getScheduledEventType() + " Road Crossing");
        nextCrossingEvent = crosswalk.generateNext();
    }

    public double generateSampleDelay() {
        return ServicePoint.getCarSpacingInterval();
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        queue.peek().setFirstInQueue(true);
        Trace.out(Trace.Level.INFO, "Jalankulkijat ylittävät tietä, aikaa kuluu: " + ServicePoint.getCarSpacingInterval());
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
        reserved = true;
    }

    public void switchCrossable() {
        crossable = !crossable;
        this.nextCrossingEvent = crosswalk.generateNext();
    }

    public boolean isCrossable() {
        return crossable;
    }

    public Event getNextCrossingEvent() {
        return nextCrossingEvent;
    }
}
