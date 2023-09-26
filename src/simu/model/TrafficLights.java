package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

public class TrafficLights extends ServicePoint {
    private ArrivalProcess trafficLight;
    private boolean greenLight = true;

    public TrafficLights(ContinuousGenerator generator, ContinuousGenerator lightSwitchFrequencyGenerator, EventList eventList, String type) {
        super(generator, eventList, type);
        trafficLight = new ArrivalProcess(lightSwitchFrequencyGenerator, eventList, type + " Light Switch");
        trafficLight.generateNext();
    }

    @Override
    public void startService() {
        reserved = true;
        Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId() + " ajaa liikennevalojen läpi.");
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }

    public void switchGreenLight() {
        greenLight = !greenLight;
        trafficLight.generateNext();
    }
}
