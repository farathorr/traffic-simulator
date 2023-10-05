package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class Roundabout extends ServicePoint {

    private ContinuousGenerator exitGenerator;
    private int maxRotations;

    private PriorityQueue<Customer> queue = new PriorityQueue<>();

    public Roundabout(ContinuousGenerator generator, ContinuousGenerator exitGenerator, EventList eventList, String type, int maxRotations) {
        super(generator, eventList, type);
        this.exitGenerator = exitGenerator;
        this.maxRotations = maxRotations;
    }

    @Override
    public void addToQueue(Customer customer) { // Override to use the PriorityQueue
        queue.add(customer);
        customer.setFirstInQueue(false);
        if (!customer.cameFromRoundabout()) return; // Render waiting queue on the road next to the roundabout

        customer.addDestination(this.getX() + (Math.random() - 0.5)/3, this.getY() + (Math.random() - 0.5)/3);
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
        //Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
        Customer selectedCustomer = queue.peek();
        selectedCustomer.setFirstInQueue(true);
        if (selectedCustomer.getRoundaboutExit() == null) {
            Roundabout currentRoundabout = this;
            do {
                int i = Math.min((int) Math.round(Math.abs(exitGenerator.sample())), maxRotations);
                for(int j = 0; j < i; j++) {
                    currentRoundabout = (Roundabout) this.getLevel().getNextRoundaboutServicePoint(currentRoundabout, false);
                }
            } while(!this.getLevel().roundaboutHasExitPoint(currentRoundabout));
            selectedCustomer.setRoundaboutExit(currentRoundabout.getScheduledEventType());
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
}