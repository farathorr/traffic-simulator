package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

public class TrafficLights extends ServicePoint {
    private ArrivalProcess trafficLight;

    public TrafficLights(ContinuousGenerator generator,ContinuousGenerator lightSwitchFrequencyGenerator, EventList eventList) {
        super(generator, eventList, EventType.TRAFFIC_LIGHTS);
        trafficLight = new ArrivalProcess(lightSwitchFrequencyGenerator, eventList, EventType.LIGHT_SWITCH);
        trafficLight.generateNext();
    }

    @Override
    public void startService() {
        Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId()+" ajaa liikennevalojen läpi.");
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
        reserved = true;
    }

    @Override
    public void switchReserved(){
        reserved = !reserved;
        trafficLight.generateNext();
    }

}
