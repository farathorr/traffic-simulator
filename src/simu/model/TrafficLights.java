package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

public class TrafficLights extends ServicePoint {

    public TrafficLights(ContinuousGenerator generator, EventList eventList) {
        super(generator, eventList, EventType.TRAFFIC_LIGHTS);
    }

    @Override
    public void startService() {
        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
    }
}
