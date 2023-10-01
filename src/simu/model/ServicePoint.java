package simu.model;

import simu.framework.*;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public abstract class ServicePoint {
    protected final LinkedList<Customer> queue = new LinkedList<>(); // Tietorakennetoteutus
    protected final ContinuousGenerator generator;
    protected final EventList eventList;
    private Level level;
    protected final String scheduledEventType;
    private double x = 0, y = 0, rotation = 0;

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

    public void addToQueue(Customer a) {   // Jonon 1. asiakas aina palvelussa
        queue.add(a);
    }


    public void startService() {
        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());

        reserved = true;
        double serviceTime = generator.sample();
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
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

    public void render(double x, double y, double rotation) {
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

    public double getRotation() {
        return rotation;
    }
}
