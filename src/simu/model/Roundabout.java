package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;

public class Roundabout extends ServicePoint {

    private ContinuousGenerator exitGenerator;
    private static final EventType[] eventTypeList = {EventType.ROUNDABOUT_BOTTOM, EventType.ROUNDABOUT_LEFT, EventType.ROUNDABOUT_TOP, EventType.ROUNDABOUT_RIGHT};


    public Roundabout(ContinuousGenerator generator, ContinuousGenerator exitGenerator, EventList eventList, EventType tyyppi) {
        super(generator, eventList, tyyppi);
        this.exitGenerator = exitGenerator;
    }

    @Override
    public void startService() {
        //Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
        Customer selectedCustomer = queue.peek();
        if (selectedCustomer.getRoundaboutExit() == null) {
            int randomIndex = (int) Math.round(Math.abs(exitGenerator.sample()));
            selectedCustomer.setRoundaboutExit(eventTypeList[randomIndex]);
        }
        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }
}