package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Liikenneympyrä luokka.
 * Luokka perii tietoja ServicePoint luokalta.
 */
public class Roundabout extends ServicePoint {
    /**
     * Tallentaa maksimi jonon pituuten SQL tietokantaa varten.
     */
    private int maxQueueSize = 0;
    /**
     * Kertoo miten monta kierrosta liikenneympyrässä on, mutta simulaatio ei oikein tue muuta arvoa kuin 3 visuaalisesti.
     * Jos lisätään enemmän kuvia, niin voit muuttaa tätä arvoa, jos tarvitse enemmän käännöksiä.
     */
    private int maxRotations = 3;

    /**
     * Oma liikenne jono jota liikenneympyrä käyttää.
     * Tämä on PriorityQueue, koska se järjestää asiakkaita, jotka ovat jo liikenneympyrässä uusien asiakkaiden eteen.
     */
    private PriorityQueue<Customer> queue = new PriorityQueue<>();

    /**
     * @param eventList Lista johon tallennetaan kaikki tapahtumat
     * @param type Palvelupisteen tyyppi
     */
    public Roundabout(EventList eventList, String type) {
        super(eventList, type);
    }

    /**
     * Lisää uusi asiakas palvelupisteen jonoon.
     * Tämä metodi on ylikirjoitettu, koska liikenneympyrä käyttää PriorityQueue jonoa.
     * @param customer Asiakas joka lisätään liikenneympyrä jonoon
     */
    @Override
    public void addToQueue(Customer customer) { // Override to use the PriorityQueue
        queue.add(customer);
        customer.setFirstInQueue(false);

        customer.addDestination(this.getX() + (Math.random() - 0.5)/5, this.getY() + (Math.random() - 0.5)/5);
    }

    /**
     * Poistaa palvelupisteen johosta ensimmäisen asiakkaan.
     * Palvelupiste asetetaan vapaaksi ja asiakkaan viimeiseksi palvelupisteeksi asetetaan tämä palvelupiste.
     * Metodi ylikirjoitetaan koska liikenneympyrä käyttää PriorityQueue jonoa.
     * @return Palauttaa jonossa olevan ensimmäisen asiakkaan.
     */
    @Override
    public Customer takeFromQueue() { // Override to use the PriorityQueue
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    /**
     * Generoi tapahtuman jolloin asiakas poistuu pisteestä.
     * Piste tarkistaa onko asiakkaalla jo määränpäätä pois liikenneympyrästä ja jos sitä ei ole generoi hänelle poistumis piste.
     * Poistumis piste on satunnainesta valittu kaikista mahdollisista poistumispeistä, sisääntuloa lukuunottamatta.
     * Tämän lisäksi asiakkaan firstInQueue arvoksi asetetaan true, jotta se voidaan piirtää canvas näytöllä vihreänä.
     * Palvelupiste asetetaan varatuksi.
     */
    @Override
    public void startService() {
        Customer selectedCustomer = queue.peek();
        selectedCustomer.setFirstInQueue(true);
        if (selectedCustomer.getRoundaboutExit() == null) {
            Roundabout currentRoundabout = this;
            do {
                int i = (int)Math.round(Math.random() * maxRotations);
                for(int j = 0; j < i; j++) {
                    currentRoundabout = (Roundabout) this.getLevel().getNextRoundaboutServicePoint(currentRoundabout, false);
                }
            } while(!this.getLevel().roundaboutHasExitPoint(currentRoundabout));
            selectedCustomer.setRoundaboutExit(currentRoundabout.getScheduledEventType());
        }
        if(this.maxQueueSize < this.getQueue().size()) {
            this.maxQueueSize = this.getQueue().size();
        }
        reserved = true;
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
    }

    /**
     * Tulostaa konsoliin miten luokka luodaan taso syntaxilla.
     * Metodia käytetään kun tehdään tasoja ja haluat exporttaa tason ulos.
     * Tämä mahdollistaa tason luomisen konsolista kopioimalla.
     */
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

    @Override
    public Customer getFirstCustomer() {
        return queue.peek();
    }

    public boolean queueNotEmpty() {
        return queue.size() != 0;
    }

    public PriorityQueue<Customer> getRoundaboutQueue() {
        return queue;
    }

    @Override
    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}