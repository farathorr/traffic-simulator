package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Luokka jalankulkijoiden ylityspaikkaa edustavalle palvelupisteelle.
 * Luokka perii ServicePoint-luokan.
 */
public class Crosswalk extends ServicePoint {
    /**
     * Tienylitystapahtumalle luodaan oma ArrivalProcess-olio.
     */
    private ArrivalProcess crosswalk;
    /**
     * Tienylityspaikka on oletuksena ylitettävissä.
     */
    private boolean crossable = true;
    /**
     * Tienylitystapahtuma on oletuksena null.
     */
    private Event nextCrossingEvent = null;
    /**
     * Tienylitystapahtumalle luodaan kaksi ContinuousGenerator-oliota, yksi määrittää tienylitystapahtumien aikavälit, ja toinen niiden pituudet.
     */
    private ContinuousGenerator crossingFrequencyGenerator, crossingTimeGenerator;
    /**
     * Tienylitystapahtumien aikavälien ja pituuksien keskiarvot ja varianssit.
     */
    private double mean, variance, mean2, variance2;
    /**
     * startService() metodissa päivitettävät kokonaisluvut, jotka lähetetään SQL-tietokantaan simulaation päätyttyä.
     */
    private int carCount, maxQueueSize;

    /**
     * @param mean Tienylitystapahtumien aikavälien keskiarvo
     * @param variance Tienylitystapahtumien aikavälien varianssi
     * @param mean2 Tienylitystapahtumien pituuksien keskiarvo
     * @param variance2 Tienylitystapahtumien pituuksien varianssi
     * @param eventList EventList-olio, joka sisältää simulaation tapahtumalistan
     * @param type Tienylitystapahtuman tyyppi
     */
    public Crosswalk(double mean, double variance, double mean2, double variance2, EventList eventList, String type) {
        super( eventList, type);
        this.mean = mean;
        this.variance = variance;
        this.mean2 = mean2;
        this.variance2 = variance2;
    }

    /**
     * Metodi, joka alustaa Crosswalk-olion.
     * Metodissa luodaan tienylitystapahtumalle ArrivalProcess-olio, joka luo tienylitystapahtumia.
     * Metodissa luodaan myös tienylitystapahtumille kaksi ContinuousGenerator-oliota, jotka määrittävät tienylitystapahtumien aikavälit ja pituudet.
     * Metodissa myös tarkistetaan, onko tienylitystapahtumalle annettu asetuksia, ja jos on, niin ne asetetaan olion käytettäviksi.
     * Metodissa myös luodaan ensimmäinen tienylitystapahtuma.
     */
    public void init() {
        if (hasSettings("mean")) mean = getSettings("mean");
        if (hasSettings("variance")) variance = getSettings("variance");
        if (hasSettings("mean2")) mean2 = getSettings("mean2");
        if (hasSettings("variance2")) variance2 = getSettings("variance2");

        crossingFrequencyGenerator = new Normal(mean, variance);
        crossingTimeGenerator = new Normal(mean2, variance2);

        if (eventList == null) return;

        crosswalk = new ArrivalProcess(crossingTimeGenerator, eventList, this.getScheduledEventType() + " Road Crossing");
        nextCrossingEvent = crosswalk.generateNext();
    }

    /**
     * @return Palauttaa ohjelmaan määritetyn autojen kulkunopeuden.
     */
    public double generateSampleDelay() {
        return ServicePoint.getCarSpacingInterval();
    }

    /**
     * Metodi, joka käsittelee yhden auton siirtymisen tienylityspaikan läpi.
     * Metodi asettaa jonon ensimmäiselle asiakkaalle tiedon siitä, että asiakas on tienylityspaikan jonossa ensimmäisenä.
     * Tapahtumalistaan lisätään tapahtuma auton poistumisesta tienylityspaikalta. Tapahtuma lisätään tapahtumalistaan auton kulkunopeuden päästä.
     * Palvelupiste asetetaan varatuksi, ja palvelupisteen läpimenneiden autojen määrää kasvatetaan yhdellä.
     * Palvelupisteen jonon maksimikoko päivitetään, jos jonon koko on suurempi kuin aiemmin.
     */
    @Override
    public void startService() {
        queue.peek().setFirstInQueue(true);
        Trace.out(Trace.Level.INFO, "Auto ylittää tien, aikaa kuluu: " + ServicePoint.getCarSpacingInterval());
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + ServicePoint.getCarSpacingInterval()));
        reserved = true;
        carCount++;
        if(this.maxQueueSize < this.getQueue().size()){
            this.maxQueueSize = this.getQueue().size();
        }
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

    /**
     * Metodi vaihtaa tienylitystapahtumien aikavälien ja pituuksien generointiin käytettävän ContinuousGenerator-olion.
     * Metodi myös vaihtaa crossable-booleanin arvon, joka kertoo onko tienylityspaikka ylitettävissä.
     * Lopulta generoidaan seuraava tienylitystapahtuma.
     */
    public void switchCrossable() {
        crosswalk.setGenerator(crossable ? crossingFrequencyGenerator : crossingTimeGenerator);
        crossable = !crossable;
        this.nextCrossingEvent = crosswalk.generateNext();
    }

    public boolean isCrossable() {
        return crossable;
    }

    public Event getNextCrossingEvent() {
        return nextCrossingEvent;
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
    public int getMaxQueueSize(){
        return maxQueueSize;
    }
}
