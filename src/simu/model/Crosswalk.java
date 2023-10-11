package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crosswalk extends ServicePoint {
    private ArrivalProcess crosswalk;
    private boolean crossable = true;
    private Event nextCrossingEvent = null;
    private ContinuousGenerator crossingFrequencyGenerator, crossingTimeGenerator;
    private double mean, variance, mean2, variance2;
    private int carCount;

    public Crosswalk(double mean, double variance, double mean2, double variance2, EventList eventList, String type) {
        super( eventList, type);
        this.mean = mean;
        this.variance = variance;
        this.mean2 = mean2;
        this.variance2 = variance2;
    }

    public void init() {
        if (hasSettings("mean")) mean = getSettings("mean");
        if (hasSettings("variance")) variance = getSettings("variance");
        if (hasSettings("mean2")) mean2 = getSettings("mean2");
        if (hasSettings("variance2")) variance2 = getSettings("variance2");

        crossingFrequencyGenerator = new Normal(mean, variance);
        crossingTimeGenerator = new Normal(mean2, variance2);

        if (eventList == null) return;

        crosswalk = new ArrivalProcess(crossingTimeGenerator, eventList, this.getScheduledEventType() + " Road Crossing");
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
        carCount++;
    }

    public void switchCrossable() {
        crosswalk.setGenerator(crossable ? crossingFrequencyGenerator : crossingTimeGenerator);
        crossable = !crossable;
        this.nextCrossingEvent = crosswalk.generateNext();
    }

    public boolean isCrossable() {
        return crossable;
    }

    public Event getNextCrossingEvent() {
        return nextCrossingEvent;
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

    public double getMean2() {
        return mean2;
    }

    public void setMean2(double mean2) {
        this.mean2 = mean2;
    }

    public double getVariance2() {
        return variance2;
    }

    public void setVariance2(double variance2) {
        this.variance2 = variance2;
    }

    public void displayClass() {
        String text = null;
        if(this.getLevel().hasNextServicePoint(this)) {
            ArrayList<String> points = this.getLevel().getAllNextServicePoints(this);
            if (points.size() == 1) {
                text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"), \"%s\");", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType, points.get(0));
            } else {
                text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"), new String[]{\"%s\"});", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType, String.join("\", \"", points));
            }
        } else {
            text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"));", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType);
        }

        System.out.println(text);
    }
    public int getCarCount() {
        return carCount;
    }
}
