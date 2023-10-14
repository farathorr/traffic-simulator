package simu.model;

import simu.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;


/**
 * Abstrakti luokka palvelupisteelle. Tämä luokka määrittelee palvelupisteen
 */
public abstract class ServicePoint {
    /**
     * LinkedList johon asiakkaat lisätään jonoon.
     * Jokaisella palvelupisteellä on oma jono.
     */
    protected final LinkedList<Customer> queue = new LinkedList<>();
    /**
     * EventList sisältää kaikki simuloinnin tapahtumat.
     */
    protected final EventList eventList;
    /**
     * Level tallentaa tiedon missä tasossa kyseinen palvelupiste on rakennettu.
     */
    private Level level;
    /**
     * scheduledEventType kertoo palvelupisteen tyypin.
     */
    protected final String scheduledEventType;
    /**
     * x ja y kertovat palvelupisteen sijainnin.
     * Tätä käytetään kun palvelupistettä piirretään canvasille.
     * x ja y eivät noudata grid koordinaatteja, eli eivät suurene 128 välein vaan nousevat 1 kerrallaan.
     */
    private double x = 0, y = 0;
    /**
     * Rotation kertoo miten palvelupiste pitäisi piirtää canvasille.
     * Esim. Tiellä on paljon eri suuntia ja ristyes versioita, joten rotation kertoo miten päin tie piirretään.
     */
    private String rotation;
    /**
     * carSpacingInterval on perus nopeus jolla autot liikkuvat simulaatiossa, jos ei ole mitään muita hidastavia tekijöitä.
     * Normaalilla tiellä liikkuminen pitäisi aina kestää 2 aika yksikköä.
     */
    private static double carSpacingInterval = 2.0;
    /**
     * reserved kertoo onko palvelupiste varattu.
     * startService() metodi asettaa palvelupisteen varatuksi ja palvelun loputtua asettaa sen vapaaksi.
     */
    protected boolean reserved = false;
    /**
     * connectionError kertoo onko palvelupisteellä yhteys ongelma.
     * Tätä käytetään palvelupistettä piirtäessä canvasille, jos palvelupisteellä ei ole oikein yhdistettyä seuraavaa pistettä.
     * Tämä saattaa tapahtua, jos tason on tehty väärin, tai editorissa seuraavaksi asetetun palvelupiste poistetaan.
     */
    private boolean connectionError = false;
    /**
     * MaxQueueSize kertoo maksimi autojen määrän jonossa.
     * Tätä käytetään SQL kannassa, kun saat simulaation suoritus tulokset ulos.
     */
    private int maxQueueSize = 0;

    /**
     * @param eventList Lista johon tallennetaan kaikki tapahtumat
     * @param type Palvelupisteen tyyppi
     */
    public ServicePoint(EventList eventList, String type) {
        this.eventList = eventList;
        this.scheduledEventType = type;
    }

    /**
     * Alustaa palvelupisteen.
     * Tätä ei tarvita kaikissa palvelupisteissä, mutta liikennevalot ja suojatiet ylikirjoittavat tämän.
     */
    public void init() {

    }

    /**
     * Lisää asiakkaan jonoon.
     * Asettaa asiakkaan visuaaliseksi sijainniksi palvelupisteen sijainnin.
     * Ase ettei asiakas ole ensimmäinen jonossa.
     * @param customer Asiakas joka lisätään jonoon
     */
    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.addDestination(x + (Math.random() - 0.5)/5, y + (Math.random() - 0.5)/5);
        customer.setFirstInQueue(false);
    }

    /**
     * Poistaa palvelupisteen johosta ensimmäisen asiakkaan.
     * Palvelupiste asetetaan vapaaksi ja asiakkaan viimeiseksi palvelupisteeksi asetetaan tämä palvelupiste.
     * @return Palauttaa jonossa olevan ensimmäisen asiakkaan.
     */
    public Customer takeFromQueue() {
        reserved = false;
        Customer selectedCustomer = queue.poll();
        selectedCustomer.setLastServicePoint(scheduledEventType);
        return selectedCustomer;
    }

    /**
     * Generoi tapahtuman jolloin asiakas poistuu pisteestä
     * Asettaa asiakkaan firstInQueue arvoksi true, jotta se voidaan piirtää canvas näytöllä vihreänä.
     * Palvelupiste asetetaan varatuksi.
     */
    public void startService() {
        Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
        queue.peek().setFirstInQueue(true);
        reserved = true;
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + carSpacingInterval));
    }

    /**
     * Asettaa palvelupisteelle kaikki tarvittavat tiedot jotta se voidaan piirtää canvasille.
     * @param x Palvelupisteen x koordinaatti
     * @param y Palvelupisteen y koordinaatti
     * @param rotation Palvelupisteen rotation
     */
    public void render(double x, double y, String rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
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


    /**
     * Tulostaa konsoliin miten palvelupiste piirretään tasoon.
     * Tätä käytetään kun haluat exporttaa tasoja ulos editorista.
     */
    public void displayClassRender() {
        System.out.printf("controller.render(level, \"%s\", %.0f, %.0f, \"%s\");\n", this.scheduledEventType, this.x, this.y, this.rotation);
    }

    /**
     * @return Palauttaa palvelupisteen tiedot merkkijonona.
     */
    public String toString() {
        return String.format("%s (%.0f, %.0f) ", this.getClass().getSimpleName(), x, y);
    }

    public Customer getFirstCustomer() {
        return queue.peek();
    }

    public boolean queueNotEmpty() {
        return queue.size() != 0;
    }

    public boolean isConnectionError() {
        return connectionError;
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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

    public LinkedList<Customer> getQueue() {
    	return queue;
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

    public int getMaxQueueSize(){return 0;}

    public void setMaxQueueSize(int newMax){
        this.maxQueueSize = newMax;
    }
}
