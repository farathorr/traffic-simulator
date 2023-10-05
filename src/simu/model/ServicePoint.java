package simu.model;

import simu.framework.*;

import java.util.LinkedList;
import java.util.PriorityQueue;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public abstract class ServicePoint {
    protected final LinkedList<Customer> queue = new LinkedList<>(); // Tietorakennetoteutus
    protected final ContinuousGenerator generator;
    protected final EventList eventList;
    private Level level;
    protected final String scheduledEventType;
    private double x = 0, y = 0;
    private String rotation;
    private static double carSpacingInterval = 2.0;
    protected boolean reserved = false;

    public ServicePoint(ContinuousGenerator generator, EventList eventList, String type) {
        this.eventList = eventList;
        this.generator = generator;
        this.scheduledEventType = type;
    }

    public Customer takeFromQueue() {  // Poistetaan palvelussa ollut
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.setDestinationX(x + Math.random() - 0.5);
        customer.setDestinationY(y + Math.random() - 0.5);
        customer.setFirstInQueue(false);
    }


    public void startService() {
        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
        queue.peek().setFirstInQueue(true);
        reserved = true;
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + carSpacingInterval));
    }

    public static void setCarSpacingInterval(double carSpacingInterval) {
        ServicePoint.carSpacingInterval = carSpacingInterval;
    }

    public static double getCarSpacingInterval() {
        return carSpacingInterval;
    }

    public boolean isReserved() {
        return reserved;
    }

    public String getScheduledEventType() {
        return scheduledEventType;
    }

    public boolean queueNotEmpty() {
        return queue.size() != 0;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void render(double x, double y, String rotation) {
    	this.x = x;
    	this.y = y;
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getRotation() {
        return rotation;
    }

    public Customer getFirstCustomer() {
    	return queue.peek();
    }

    public LinkedList<Customer> getQueue() {
    	return queue;
    }
}
