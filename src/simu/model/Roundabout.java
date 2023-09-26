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

    private PriorityQueue<Customer> queue = new PriorityQueue<>();

    public Roundabout(ContinuousGenerator generator, ContinuousGenerator exitGenerator, EventList eventList, String tyyppi) {
        super(generator, eventList, tyyppi);
        this.exitGenerator = exitGenerator;
    }

    @Override
    public void addToQueue(Customer a) {   // Jonon 1. asiakas aina palvelussa
        queue.add(a);
    }

    @Override
    public Customer takeFromQueue() {  // Poistetaan palvelussa ollut
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    @Override
    public boolean queueNotEmpty() {
        return queue.size() != 0;
    }

    @Override
    public void startService() {
        //Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
        Customer selectedCustomer = queue.peek();
        if (selectedCustomer.getRoundaboutExit() == null) {
            do {
                int randomIndex = Math.min((int) Math.round(Math.abs(exitGenerator.sample())),3);
                selectedCustomer.setRoundaboutExit(eventTypeList[randomIndex]);
            } while (selectedCustomer.getRoundaboutExit() == scheduledEventType);
        }

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
    }
}