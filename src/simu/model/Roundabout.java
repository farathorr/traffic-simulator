package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Roundabout extends ServicePoint {

    private ContinuousGenerator exitGenerator;
    private int maxQueueSize = 0;
    private int maxRotations = 3;
    private double mean, variance;
    private PriorityQueue<Customer> queue = new PriorityQueue<>();

    public Roundabout(double mean, double variance, EventList eventList, String type, int maxRotations) {
        super(eventList, type);
        this.mean = mean;
        this.variance = variance;
        this.exitGenerator = new Normal(mean, variance);
    }

    @Override
    public void addToQueue(Customer customer) { // Override to use the PriorityQueue
        queue.add(customer);
        customer.setFirstInQueue(false);

        customer.addDestination(this.getX() + (Math.random() - 0.5)/5, this.getY() + (Math.random() - 0.5)/5);
    }

    @Override
    public boolean queueNotEmpty() {
        return queue.size() != 0;
    }

    @Override
    public Customer takeFromQueue() { // Override to use the PriorityQueue
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    @Override
    public void startService() {
        Customer selectedCustomer = queue.peek();
        selectedCustomer.setFirstInQueue(true);
        if (selectedCustomer.getRoundaboutExit() == null) {
            Roundabout currentRoundabout = this;
            do {
                int i = (int)Math.round(Math.random() * maxRotations);
                for(int j = 0; j < i; j++) {
                    currentRoundabout = (Roundabout) this.getLevel().getNextRoundaboutServicePoint(currentRoundabout, false);
                }
            } while(!this.getLevel().roundaboutHasExitPoint(currentRoundabout));
            selectedCustomer.setRoundaboutExit(currentRoundabout.getScheduledEventType());
        }
        if(this.maxQueueSize < this.getQueue().size()){
            this.maxQueueSize = this.getQueue().size();
        }
        reserved = true;
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
    }

    @Override
    public Customer getFirstCustomer() {
        return queue.peek();
    }

    public PriorityQueue<Customer> getRoundaboutQueue() {
        return queue;
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
                text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\", 3), \"%s\");", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType, points.get(0));
            } else {
                text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\", 3), new String[]{\"%s\"});", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType, String.join("\", \"", points));
            }
        } else {
            text = String.format("level.add(new %s(%.0f, %.0f, eventList, \"%s\", 3));", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.scheduledEventType);
        }

        System.out.println(text);
    }

    @Override
    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}