package simu.model;

import simu.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public abstract class ServicePoint {
    protected final LinkedList<Customer> queue = new LinkedList<>(); // Tietorakennetoteutus
    protected final EventList eventList;
    private Level level;
    protected final String scheduledEventType;
    private double x = 0, y = 0;
    private String rotation;
    private static double carSpacingInterval = 2.0;
    protected boolean reserved = false;
    private boolean connectionError = false;

    public ServicePoint(EventList eventList, String type) {
        this.eventList = eventList;
        this.scheduledEventType = type;
    }

    public void init() {

    }

    public Customer takeFromQueue() {  // Poistetaan palvelussa ollut
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }

    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.addDestination(x + (Math.random() - 0.5)/5, y + (Math.random() - 0.5)/5);
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

    public String toString() {
        return String.format("%s (%.0f, %.0f) ", this.getClass().getSimpleName(), x, y);
    }

    public void displayClass() {
        String text = null;
        if(this.getLevel().hasNextServicePoint(this)) {
            ArrayList<String> points = this.getLevel().getAllNextServicePoints(this);
            if (points.size() == 1) {
                text = String.format("level.add(new %s(eventList, \"%s\"), \"%s\");", this.getClass().getSimpleName(), this.scheduledEventType, points.get(0));
            } else {
                text = String.format("level.add(new %s(eventList, \"%s\"), new String[]{\"%s\"});", this.getClass().getSimpleName(), this.scheduledEventType, String.join("\", \"", points));
            }
        } else {
            text = String.format("level.add(new %s(eventList, \"%s\"));", this.getClass().getSimpleName(), this.scheduledEventType);
        }

        System.out.println(text);
    }

    public void displayClassRender() {
        System.out.printf("controller.render(level, \"%s\", %.0f, %.0f, \"%s\");\n", this.scheduledEventType, this.x, this.y, this.rotation);
    }

    public boolean hasSettings(String key) {
    	return LevelSettings.getInstance().has(level.getLevelName() + "." + this.scheduledEventType + "." + key);
    }

    public double getSettings(String key) {
        return LevelSettings.getInstance().get(level.getLevelName() + "." + this.scheduledEventType + "." + key);
    }

    public void setSettings(String key, double value) {
    	LevelSettings.getInstance().add(level.getLevelName() + "." + this.scheduledEventType + "." + key, value);
    }

    public double getMean() {
    	return 0.0;
    }

    public double getVariance() {
    	return 0.0;
    }

    public double getMean2() {
        return 0.0;
    }

    public double getVariance2() {
        return 0.0;
    }

    public int getCarCount() {
    	return 0;
    }
}
