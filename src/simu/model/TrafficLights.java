package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

import java.util.ArrayList;

public class TrafficLights extends ServicePoint {
    private ArrivalProcess trafficLight;
    private boolean greenLight = true;
    private Event nextLightSwitchEvent = null;
    private ContinuousGenerator lightSwitchFrequencyGenerator;

    private double mean, variance;

    public TrafficLights(double mean, double variance, EventList eventList, String type) {
        super(eventList, type);
        this.mean = mean;
        this.variance = variance;
        this.lightSwitchFrequencyGenerator = new Normal(mean, variance);
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

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public void displayClass() {
        String text = null;
        if(this.getLevel().hasNextServicePoint(this)) {
            ArrayList<String> points = this.getLevel().getAllNextServicePoints(this);
            if (points.size() == 1) {
                text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\"), \"%s\");", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType, points.get(0));
            } else {
                text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\"), new String[]{\"%s\"});", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType, String.join("\", \"", points));
            }
        } else {
            text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\"));", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType);
        }

        System.out.println(text);
    }
}
