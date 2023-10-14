package simu.model;

import lib.Rounding;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

/**
 * Luokka joka mallintaa maalialueen.
 * Maalialue on palvelupiste joka on asiakkaiden viimein määränpää.
 */
public class Goal extends ServicePoint {
    /**
     * @param eventList EventList-olio, joka sisältää simulaation tapahtumalistan
     * @param type Palvelupisteen tyyppi, eli tässä tapauksessa "goal".
     */
    public Goal(EventList eventList, String type) {
        super(eventList, type);
    }

    /**
     * Double-arvo joka kertoo ajan, jonka asiakas on viettänyt simulaatiossa.
     */
    private double totalTime;
    /**
     * Kokonaisluku joka kertoo montako autoa on saapunut maalialueelle.
     */
    private int carCount = 0;

    /**
     * Metodi joka käynnistää palvelun.
     * Palvelupiste on varattu palvelun aikana.
     * Asiakas on jonossa palvelun aikana.
     * Palvelun kesto on 2.
     * Asiakas asetetaan jonon ensimmäiseksi ja asiakas saavuttaa määränpäänsä.
     * Asiakkaan saapumisajan ja nykyisen ajan erotus lisätään totalTime-muuttujaan.
     * Autojen määrä kasvaa yhdellä.
     * @see simu.framework.ServicePoint#startService()
     */
    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        Customer customer = queue.peek();
        carCount++;
        totalTime += Clock.getInstance().getTime() - customer.getArrivalTime();
        customer.setFirstInQueue(true);
        customer.setReachedGoal(true);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + 2));
    }

    /**
     * @param customer Asiakas joka lisätään jonoon ja jolle asetetaan määränpää visuaalista esitystä varten.
     */
    @Override
    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.addDestination(this.getX() + (Math.random() - 0.5), this.getY() + (Math.random() - 0.5));
        customer.setFirstInQueue(false);
    }

    /**
     * @return Palauttaa asiakkaiden yhteisen odotusajan keskiarvon.
     */
    public double getAverageCompletionTime() {
        if(carCount == 0) return 0;
        System.out.println("carCount: " + carCount);
        System.out.println("totalTime: " + totalTime);
        return Rounding.toFixed((totalTime/carCount),2);
    }

    public int getCarCount() {
        return carCount;
    }
}
