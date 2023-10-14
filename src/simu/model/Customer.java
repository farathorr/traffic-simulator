package simu.model;

import lib.Rounding;
import simu.framework.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Luokka simulaatiossa liikkuville asiakkaille, tämän simulaation asiakkaat ovat autoja.
 * Luokka toteuttaa Comparable-rajapinnan, jotta asiakkaat voidaan järjestää saapumisjärjestyksen mukaan.
 */
public class Customer implements Comparable<Customer> {
    /**
     * Double, joka kertoo asiakkaan saapumisajan simulaatioon.
     */
    private double arrivalTime;
    /**
     * Double, joka kertoo asiakkaan poistumisajan simulaatiosta.
     */
    private double leavingTime;
    /**
     * Kokonaisluku, joka kertoo asiakkaan id:n.
     */
    private int id;
    /**
     * Level-olio, joka kertoo missä tasossa asiakas on.
     */
    private Level level;
    private static int customerCount = 1;
    private static long sum = 0;
    private String lastServicePoint = null;

    private String roundaboutExit = null;
    private double x, y, destinationX, destinationY;
    private boolean firstInQueue = false;
    private boolean reachedGoal = false;
    private boolean canDelete = false;

    private List<Double[]> destinations = new ArrayList<>();

    public Customer(Level level, double x, double y) {
        id = customerCount++;
        this.level = level;
        this.x = x;
        this.y = y;
        this.destinationX = x;
        this.destinationY = y;

        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo " + arrivalTime);
    }

    public void setDestinationX(double destinationX) {
        this.destinationX = destinationX;
    }

    public void setDestinationY(double destinationY) {
        this.destinationY = destinationY;
    }

    public double getDestinationX() {
        return destinationX;
    }

    public double getDestinationY() {
        return destinationY;
    }

    public static void resetCustomerCount() {
        customerCount = 1;
    }

    public void setLeavingTime(double leavingTime) {
        this.leavingTime = leavingTime;
    }

    public double getLeavingTime() {
        return leavingTime;
    }

    public double getWaitingTime() {
        return Rounding.toFixed(leavingTime - arrivalTime, 2);
    }

    public int getId() {
        return id;
    }

    public boolean isReachedGoal() {
        return reachedGoal;
    }

    public void setReachedGoal(boolean reachedGoal) {
        this.reachedGoal = reachedGoal;
    }

    public void report() {
        Trace.out(Trace.Level.INFO, "\nAuto " + id + " valmis! ");
        Trace.out(Trace.Level.INFO, "Auto " + id + " saapui: " + arrivalTime);
        Trace.out(Trace.Level.INFO, "Auto " + id + " poistui: " + leavingTime);
        Trace.out(Trace.Level.INFO, "Auto " + id + " viipyi: " + getWaitingTime());
        sum += getWaitingTime();
        double average = sum / id;
        Trace.out(Trace.Level.INFO, "Autojen läpimenoaikojen keskiarvo tähän asti " + average);
    }

    public String getRoundaboutExit() {
        return roundaboutExit;
    }

    public void setRoundaboutExit(String roundaboutExit) {
        this.roundaboutExit = roundaboutExit;
    }

    public void setLastServicePoint(String lastServicePoint) {
        this.lastServicePoint = lastServicePoint;
    }


    @Override
    public int compareTo(Customer arg) {
//        if (this.time < arg.time) return -1;
//        else if (this.time > arg.time) return 1;
//        return 0;
        if (cameFromRoundabout(this) == cameFromRoundabout(arg)) {
            return 0;
        } else {
            if (cameFromRoundabout(this)) return -1;
            else return 1;
        }
    }

    public boolean isFirstInQueue() {
        return firstInQueue;
    }

    public void setFirstInQueue(boolean firstInQueue) {
        this.firstInQueue = firstInQueue;
    }

    private boolean cameFromRoundabout(Customer customer) {
        if (customer.level.getServicePoint(lastServicePoint) != null) {
            return customer.level.getServicePoint(lastServicePoint).getClass() == Roundabout.class;
        } return false;
    }

    public boolean cameFromRoundabout() {
        return cameFromRoundabout(this);
    }

    public void addDestination(double x, double y){
        Double[] destination = new Double[2];
        destination[0] = x;
        destination[1] = y;
        if (destinations.isEmpty()){
            this.destinationX = x;
            this.destinationY = y;
        }
        destinations.add(destination);
    }

    public void moveCustomer() {
        if (destinations.isEmpty()) return;
        double speed = Math.max(20 / (destinations.size() * 2), 1);

        double deltaX = destinationX - this.x;
        double deltaY = destinationY - this.y;
        this.x += deltaX / speed;
        this.y += deltaY / speed;

        if (Math.abs(this.destinationX - this.x) + Math.abs(this.destinationY - this.y) < 0.1) {
            if (destinations.size() > 1) {
                destinations.remove(0);
                this.destinationX = destinations.get(0)[0];
                this.destinationY = destinations.get(0)[1];
            } else if(reachedGoal) canDelete = true;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public static int getCustomerCount() {
        return customerCount;
    }

    public boolean canDelete() {
        return canDelete;
    }
}
