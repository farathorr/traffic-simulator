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
    /**
     * Kokonaisluku, joka kertoo montako asiakasta on luotu.
     */
    private static int customerCount = 1;
/**
     * Kokonaisluku, jota käytetään asiakkaiden yhteisen odotusajan keskiarvon laskemiseen..
     */
    private static long sum = 0;
    /**
     * String, joka kertoo asiakkaan viimeisimmän palvelupisteen.
     */
    private String lastServicePoint = null;
    /**
     * String, joka kertoo asiakkaan poistumisreitin liikenneympyrässä.
     */
    private String roundaboutExit = null;
    /**
     * Asiakkaan x-koordinaatti.
     * Asiakkaan y-kordinaatti.
     * Asiakkaan määränpään x-koordinaatti.
     * Asiakkaan määränpään y-koordinaatti.
     */
    private double x, y, destinationX, destinationY;
    /**
     * Boolean, joka kertoo onko asiakas jonon ensimmäinen.
     */
    private boolean firstInQueue = false;
    /**
     * Boolean, joka kertoo onko asiakas saavuttanut määränpäänsä.
     */
    private boolean reachedGoal = false;
    /**
     * Boolean, joka kertoo onko asiakas valmis poistumaan simulaatiosta.
     */
    private boolean canDelete = false;
    /**
     * Lista, joka sisältää asiakkaan määränpään x ja y kordinaatit.
     */
    private List<Double[]> destinations = new ArrayList<>();

    /**
     * @param level Tasoluokka, jossa asiakas on.
     * @param x Asiakkaan x-koordinaatti.
     * @param y Asiakkaan y-koordinaatti.
     */
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

    /**
     * Tulostaa asiakkaan tiedot konsoliin.
     */
    public void report() {
        Trace.out(Trace.Level.INFO, "\nAuto " + id + " valmis! ");
        Trace.out(Trace.Level.INFO, "Auto " + id + " saapui: " + arrivalTime);
        Trace.out(Trace.Level.INFO, "Auto " + id + " poistui: " + leavingTime);
        Trace.out(Trace.Level.INFO, "Auto " + id + " viipyi: " + getWaitingTime());
        sum += getWaitingTime();
        double average = sum / id;
        Trace.out(Trace.Level.INFO, "Autojen läpimenoaikojen keskiarvo tähän asti " + average);
    }

    /**
     * @param arg Asiakas, johon verrataan.
     * @return Palauttaa 0, jos asiakkaat tulivat liikenneympyrästä, -1 jos tämä asiakas tuli liikenneympyrästä ja arg ei, ja 1 jos tämä asiakas ei tullut liikenneympyrästä ja arg tuli.
     */
    @Override
    public int compareTo(Customer arg) {
        if (cameFromRoundabout(this) == cameFromRoundabout(arg)) {
            return 0;
        } else {
            if (cameFromRoundabout(this)) return -1;
            else return 1;
        }
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

    public String getRoundaboutExit() {
        return roundaboutExit;
    }

    public void setRoundaboutExit(String roundaboutExit) {
        this.roundaboutExit = roundaboutExit;
    }

    public void setLastServicePoint(String lastServicePoint) {
        this.lastServicePoint = lastServicePoint;
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
