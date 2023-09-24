package simu.model;

import lib.Rounding;
import simu.framework.*;

// TODO:
// Customer koodataan simulointimallin edellyttämällä tavalla (data!)
public class Customer implements Comparable<Customer> {
    private double arrivalTime;
    private double leavingTime;
    private int id;
    private static int customerCount = 1;
    private static long sum = 0;
    private EventType lastServicePoint = null;

    private EventType roundaboutExit = null;

    public Customer() {
        id = customerCount++;

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
        System.out.println("Autojen läpimenoaikojen keskiarvo tähän asti " + average);
    }

    public EventType getRoundaboutExit() {
        return roundaboutExit;
    }

    public void setRoundaboutExit(EventType roundaboutExit) {
        this.roundaboutExit = roundaboutExit;
    }

    public void setLastServicePoint(EventType lastServicePoint) {
        this.lastServicePoint = lastServicePoint;
    }

    @Override
    public int compareTo(Customer arg) {
//        if (this.time < arg.time) return -1;
//        else if (this.time > arg.time) return 1;
//        return 0;
        if (isRoundAbout(this) == isRoundAbout(arg)) {
            return 0;
        } else {
            if (isRoundAbout(this)) return -1;
            else return 1;
        }
    }

    private boolean isRoundAbout(Customer customer) {
        return customer.lastServicePoint == EventType.ROUNDABOUT_BOTTOM ||
                customer.lastServicePoint == EventType.ROUNDABOUT_LEFT ||
                customer.lastServicePoint == EventType.ROUNDABOUT_TOP ||
                customer.lastServicePoint == EventType.ROUNDABOUT_RIGHT;
    }
}
