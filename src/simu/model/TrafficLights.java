package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

import java.util.ArrayList;

/**
 * Liikenne valo palvelupiste.
 * Luokka perii ServicePoint luokan.
 */
public class TrafficLights extends ServicePoint {
    /**
     * Prosessi joka vaihtaa ajoittaa valojen vaihtumisen.
     */
    private ArrivalProcess trafficLight;
     /**
     * Kertoo valon tilan.
     */
    private boolean greenLight = true;
    /**
     * Tallentaa seuraavan valojen vaihtumisen.
     */
    private Event nextLightSwitchEvent = null;
    /**
     * Normaali generaattori, joka generoi luvun, joka pitää vihreätä ja punaista valoa päällä.
     */
    private ContinuousGenerator greenLightDurationGenerator, redLightDurationGenerator;
    /**
     * carCount kertoo autojen määrän ja MaxQueueSize kertoo maksimi autojen määrän jonossa.
     */
    private int carCount, maxQueueSize;

    /**
     * mean, variance, mean2 ja variance2 kertovat valojen vaihtumisen keskiarvon ja varianssin.
     */
    private double mean, variance, mean2, variance2;

    /**
     * @param mean Vihreän valon vaihtumis keskiarvo
     * @param variance Vihreän valon vaihtumis varianssi
     * @param mean2 Punaisen valon vaihtumis keskiarvo
     * @param variance2 Punaisen valon vaihtumis varianssi
     * @param eventList Lista johon tallennetaan kaikki tapahtumat
     * @param type Palvelupisteen tyyppi
     */
    public TrafficLights(double mean, double variance, double mean2, double variance2, EventList eventList, String type) {
        super(eventList, type);
        this.mean = mean;
        this.variance = variance;
        this.mean2 = mean2;
        this.variance2 = variance2;
    }

    /**
     * Alustaa palvelupisteen.
     * Alustus hakee valojen vaihtelu arvot asetuksista, jos ne on asetettu.
     * Alustus luo myös valojen vaihtumis prosessin.
     */
    public void init() {
        if (hasSettings("mean")) mean = getSettings("mean");
        if (hasSettings("variance")) variance = getSettings("variance");
        if (hasSettings("mean2")) mean2 = getSettings("mean2");
        if (hasSettings("variance2")) variance2 = getSettings("variance2");
        greenLightDurationGenerator = new Normal(mean, variance);
        redLightDurationGenerator = new Normal(mean2, variance2);

        if (eventList == null) return;

        trafficLight = new ArrivalProcess(greenLightDurationGenerator, eventList, this.getScheduledEventType() + " Light Switch");
        nextLightSwitchEvent = trafficLight.generateNext();
    }

    /**
     * Generoi tapahtuman jolloin asiakas poistuu pisteestä
     * Asettaa asiakkaan firstInQueue arvoksi true, jotta se voidaan piirtää canvas näytöllä vihreänä.
     */
    @Override
    public void startService() {
        reserved = true;
        queue.peek().setFirstInQueue(true);
        Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId() + " ajaa liikennevalojen läpi.");
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
        carCount++;
        if(this.maxQueueSize < this.getQueue().size()){
            this.maxQueueSize = this.getQueue().size();
        }
    }

    /**
     * Vaihtaa valojen tilaa vihrestä punaiseksi tai punaisesta vihreäksi.
     */
    public void switchGreenLight() {
        trafficLight.setGenerator(greenLight ? redLightDurationGenerator : greenLightDurationGenerator);
        greenLight = !greenLight;
        nextLightSwitchEvent = trafficLight.generateNext();
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
                text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"), \"%s\");", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType, points.get(0));
            } else {
                text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"), new String[]{\"%s\"});", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType, String.join("\", \"", points));
            }
        } else {
            text = String.format("level.add(new %s(%.0f, %.0f, %.0f, %.0f, eventList, \"%s\"));", this.getClass().getSimpleName(), this.getMean(), this.getVariance(), this.getMean2(), this.getVariance2(), this.scheduledEventType);
        }

        System.out.println(text);
    }

    public Event getNextLightSwitchEvent() {
        return nextLightSwitchEvent;
    }

    public boolean isGreenLight() {
        return greenLight;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getMean2() {
        return mean2;
    }

    public void setMean2(double mean2) {
        this.mean2 = mean2;
    }

    public double getVariance2() {
        return variance2;
    }

    public void setVariance2(double variance2) {
        this.variance2 = variance2;
    }

    public int getCarCount() {
        return carCount;
    }

    @Override
    public int getMaxQueueSize(){return maxQueueSize;}
}
