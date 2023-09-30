package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Roundabout extends ServicePoint {

    private ContinuousGenerator exitGenerator;
    private static final String[] eventTypeList = {"ROUNDABOUT_BOTTOM", "ROUNDABOUT_LEFT", "ROUNDABOUT_TOP", "ROUNDABOUT_RIGHT"};
    private int maxRotations;

    private PriorityQueue<Customer> queue = new PriorityQueue<>();

    public Roundabout(ContinuousGenerator generator, ContinuousGenerator exitGenerator, EventList eventList, String type, int maxRotations) {
        super(generator, eventList, type);
        this.exitGenerator = exitGenerator;
        this.maxRotations = maxRotations;
    }

    @Override
    public void addToQueue(Customer a) { // Override to use the PriorityQueue
        queue.add(a);
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
        if (selectedCustomer.getRoundaboutExit() == null) {
            int i = Math.min((int) Math.round(Math.abs(exitGenerator.sample())), maxRotations);
            Roundabout currentRoundabout = this;
            for(int j = 0; j < i; j++) {
                currentRoundabout = (Roundabout) this.getLevel().getNextRoundaboutServicePoint(currentRoundabout, false);
            }
            selectedCustomer.setRoundaboutExit(currentRoundabout.getScheduledEventType());
//            do {
//                int randomIndex = Math.min((int) Math.round(Math.abs(exitGenerator.sample())), 3);
//                selectedCustomer.setRoundaboutExit(eventTypeList[randomIndex]);
//            } while (selectedCustomer.getRoundaboutExit() == scheduledEventType);
        }

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }
}