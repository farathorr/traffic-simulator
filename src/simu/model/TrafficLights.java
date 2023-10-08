package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

public class TrafficLights extends ServicePoint {
    private ArrivalProcess trafficLight;
    private boolean greenLight = true;
    private Event nextLightSwitchEvent = null;
    private ContinuousGenerator lightSwitchFrequencyGenerator;

    public TrafficLights(ContinuousGenerator generator, ContinuousGenerator lightSwitchFrequencyGenerator, EventList eventList, String type) {
        super(generator, eventList, type);
        this.lightSwitchFrequencyGenerator = lightSwitchFrequencyGenerator;
    }

    public void init() {
        trafficLight = new ArrivalProcess(lightSwitchFrequencyGenerator, eventList, this.getScheduledEventType() + " Light Switch");
        nextLightSwitchEvent = trafficLight.generateNext();
    }

    public double generateSampleDelay() {
        return ServicePoint.getCarSpacingInterval();
    }

    @Override
    public void startService() {
        reserved = true;
        queue.peek().setFirstInQueue(true);
        Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId() + " ajaa liikennevalojen läpi.");
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
    }

    public void switchGreenLight() {
        greenLight = !greenLight;
        nextLightSwitchEvent = trafficLight.generateNext();
    }

    public Event getNextLightSwitchEvent() {
        return nextLightSwitchEvent;
    }

    public boolean isGreenLight() {
        return greenLight;
    }
}
