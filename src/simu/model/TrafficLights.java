package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

public class TrafficLights extends ServicePoint {
    private ArrivalProcess trafficLight;

    public TrafficLights(ContinuousGenerator generator, EventList eventList) {
        super(generator, eventList, EventType.TRAFFIC_LIGHTS);
        trafficLight = new ArrivalProcess(new Normal(3,1), eventList, EventType.LIGHT_SWITCH);
        trafficLight.generateNext();
    }

    @Override
    public void startService() {
        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
    }

    public void generateNextEvent() {
        trafficLight.generateNext();
    }
}
