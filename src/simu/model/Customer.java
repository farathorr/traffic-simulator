package simu.model;

import lib.Rounding;
import simu.framework.*;

// TODO:
// Customer koodataan simulointimallin edellyttämällä tavalla (data!)
public class Customer {
    private double arrivalTime;
    private double leavingTime;
    private int id;
    private static int customerCount = 1;
    private static long sum = 0;

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
}
