package simu.model;

import simu.framework.ArrivalProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Taso-luokka.
 */
public class Level {
    /**
     * HashMap johon tallennetaan kaikki tason palvelupisteet
     * Avain: Palvelupisteen tyyppi
     */
    private Map<String, ServicePoint> servicePoints = new HashMap<>();
    /**
     * HashMap johon tallennetaan kaikki sanon saapumis prosessit.
     * Avain: Saapumis prosessin tyyppi
     */
    private Map<String, ArrivalProcess> arrivalProcesses = new HashMap<>();
    /**
     * HashMap johon tallennetaan kaikki palvelupisteet ja niiden seuraavat palvelupisteet.
     * Avaimena toimii palvelupiste ja arvona on lista seuraavien palvelupisteiden tyypeistä.
     */
    private Map<Object, ArrayList<String>> nextPoints = new HashMap<>();
    /**
     * Tason nimi
     */
    private String levelName;

    /**
     * Taso luokka.
     * Tämä luokka mahdollistaa palvelupisteille monipuolisia asettamis järjestyksiä, jotka ovat taso kohtaisija.
     * @param levelName Tason nimi
     */
    public Level(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Lisää palvelupisteen tasoon.
     * Tason lisättyä palvelupiste alustetaan.
     * @param point Palvelupiste joka lisätään tasoon.
     */
    public void add(ServicePoint point) {
        servicePoints.put(point.getScheduledEventType(), point);
        point.setLevel(this);
        point.init();
    }

    /**
     * Lisää palvelupiste tasoon ja aseta palvelupisteelle seuraava piste.
     * @param point Palvelupiste joka lisätään tasoon.
     * @param nextPoint Palvelupisteen tyyppi tekstinä joka lisätään palvelupisteen jälkeen.
     */
    public void add(ServicePoint point, String nextPoint) {
        add(point, new String[]{nextPoint});
    }

    /**
     * Lisää palvelupiste tasoon ja aseta palvelupisteelle useita seuraavia pisteitä.
     * @param point Palvelupiste joka lisätään tasoon.
     * @param nextPoint Palvelupisteen tyyppi tekstinä joka lisätään palvelupisteen jälkeen.
     */
    public void add(ServicePoint point, String[] nextPoint) {
        servicePoints.put(point.getScheduledEventType(), point);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(nextPoint));
        this.nextPoints.put(point, nextPoints);
        point.setLevel(this);
        point.init();
    }

    /**
     * Poista palvelupiste tasosta
     * @param point Palvelupiste joka poistetaan tasosta.
     */
    public void remove(ServicePoint point) {
        servicePoints.remove(point.getScheduledEventType());
        nextPoints.remove(point);
    }

    /**
     * Lisää saapumispiste tasoon.
     * @param arrivalProcess Saapumis prosessi joka lisätään tasoon.
     * @param startPoint Palvelupisteen tyyppi tekstinä josta saapumis prosessi alkaa.
     */
    public void arrival(ArrivalProcess arrivalProcess, String startPoint) {
        arrival(arrivalProcess, new String[]{startPoint});
    }

    /**
     * Ase saapumis prosessi tasoon, jolla on monta aloitus sijaintia.
     * @param arrivalProcess Saapumis prosessi joka lisätään tasoon.
     * @param startPoints Palvelupisteitten tyyppi tekstit josta saapumis prosessi voi alkaa.
     */
    public void arrival(ArrivalProcess arrivalProcess, String[] startPoints) {
        arrivalProcesses.put(arrivalProcess.getScheduledEventType(), arrivalProcess);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(startPoints));
        this.nextPoints.put(arrivalProcess, nextPoints);
    }

    /**
     * Käynnistä tason simulaation.
     * Käynnistää kaikki saapumis prosessit.
     */
    public void startSimulation() {
        for (ArrivalProcess arrivalProcess : arrivalProcesses.values()) {
            arrivalProcess.generateNext();
        }
    }

    /**
     * Tarkistaa onko palvelupisteellä seuraava palvelupiste.
     * @param key Palvelupisteen tyyppi
     * @return Palauttaa true jos palvelupisteellä on seuraava palvelupiste.
     */
    public boolean hasNextServicePoint(String key) {
        ServicePoint servicePoint = servicePoints.get(key);
        return nextPoints.containsKey(servicePoint);
    }

    public boolean hasNextServicePoint(ServicePoint servicePoint) {
        return nextPoints.containsKey(servicePoint);
    }

    public Map<Object, ArrayList<String>> getNextPoints() {
        return nextPoints;
    }

    public ServicePoint getServicePoint(String key) {
        return servicePoints.get(key);
    }

    public boolean isArrivalProcess(String type) {
        return arrivalProcesses.containsKey(type);
    }

    /**
     * Palauta seuraava palvelupiste.
     * Jos palvelupisteitä on monta, valitse satunnainen palvelupiste ja palauta se.
     * @param servicePoint Palvelupiste josta haetaan seuraava palvelupiste.
     * @return Palauttaa seuraavan palvelupisteen.
     */
    public ServicePoint getNextServicePoint(Object servicePoint) {
        int r = (int) Math.floor(Math.random() * nextPoints.get(servicePoint).size());
        return servicePoints.get(nextPoints.get(servicePoint).get(r));
    }

    public ArrayList<String> getAllNextServicePoints(Object servicePoint) {
        return nextPoints.get(servicePoint);
    }

    /**
     * Etsii seuraavan palvelupisteen liikenneympyrästä.
     * @param servicePoint Palvelupiste josta haetaan seuraava palvelupiste.
     * @param isExiting Kertoo halutaanko palvelupisteestä etsiä poistumis palvelupistettä vai seuraavaa liikenneympyrä pistettä.
     * @return Palauttaa seuraavan palvelupisteen.
     */
    public ServicePoint getNextRoundaboutServicePoint(Object servicePoint, boolean isExiting) {
        if (isExiting) {
            int r = (int) Math.ceil(Math.random() * (nextPoints.get(servicePoint).size() - 1));
            return servicePoints.get(nextPoints.get(servicePoint).get(r));
        }
        return servicePoints.get(nextPoints.get(servicePoint).get(0));
    }

    /**
     * Tarkista onko liikenneympyrällä poistumis reittiä.
     * Liikenneympyrällä ei välttämättä ole aina poistumis reittiä, joten se pitää tarkistaa.
     * @param servicePoint Liikenneympyrä josta tarkistetaan onko sillä poistumis reittiä.
     * @return Palauttaa true jos liikenneympyrällä on poistumis reitti.
     */
    public boolean roundaboutHasExitPoint(Object servicePoint) {
        return nextPoints.get(servicePoint).size() > 1;
    }

    /**
     * Laske monta seuraavaa palvelupistettä palvelupisteellä on.
     * @param key Palvelupisteen tyyppi
     * @return Palauttaa seuraavien palvelupisteiden määrän.
     */
    public int getNextServicePointCount(String key) {
        ServicePoint servicePoint = servicePoints.get(key);
        if (nextPoints.containsKey(servicePoint)) return nextPoints.get(servicePoint).size();
        return 0;
    }

    public ArrivalProcess getArrivalProcess(String key) {
        return arrivalProcesses.get(key);
    }

    public ArrayList<ServicePoint> getServicePoints() {
        return new ArrayList<>(servicePoints.values());
    }

    public String getLevelName() {
        return levelName;
    }
}
