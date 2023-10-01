package simu.model;

import lib.Rounding;
import simu.framework.*;

// TODO:
// Customer koodataan simulointimallin edellyttämällä tavalla (data!)
public class Customer implements Comparable<Customer> {
    private double arrivalTime;
    private double leavingTime;
    private int id;
    private Level level;
    private static int customerCount = 1;
    private static long sum = 0;
    private String lastServicePoint = null;

    private String roundaboutExit = null;
    private double x, y, destinationX, destinationY;

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

    public static void resetCustomerCount() {
        customerCount = 1;
    }

    public void setLeavingTime(double leavingTime) {
        this.leavingTime = leavingTime;
    }

    public double getWaitingTime() {
        return Rounding.toFixed(leavingTime - arrivalTime, 2);
    }

    public int getId() {
        return id;
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

    private boolean cameFromRoundabout(Customer customer) {
        if (customer.level.getServicePoint(lastServicePoint) != null) {
            return customer.level.getServicePoint(lastServicePoint).getClass().getSimpleName().equals("Roundabout");
        } return false;
    }

    public void setDestinationX(double destinationX) {
        this.destinationX = destinationX;
    }

    public void setDestinationY(double destinationY) {
        this.destinationY = destinationY;
    }

    public void moveCustomer() {
        double deltaX = destinationX - this.x;
        double deltaY = destinationY - this.y;
        this.x += deltaX / 20;
        this.y += deltaY / 20;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
