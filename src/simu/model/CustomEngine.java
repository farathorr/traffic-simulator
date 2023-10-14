package simu.model;

import controller.IControllerForM;
import simu.framework.*;
import eduni.distributions.Normal;

/**
 * CustomEngine on simulaattorin moottori, joka sisältää simulaation pääsilmukan.
 * CustomEngine perii Engine-luokan.
 * CustomEngine-luokka vastaa simulaation B- ja C-vaiheen tapahtumista.
 */
public class CustomEngine extends Engine {
    /**
     * Simulaation nykyinen taso.
     */
    private Level currentLevel;
    /**
     * LevelController-olio, joka sisältää simulaation tasot.
     */
    private LevelController levelController;

    /**
     * @param controller Ohjelman kontrolleri, joka haetaan IControllerForM-rajapinnan avulla
     * @param levelKey Simulaation nykyinen taso
     * Konstruktorissa luodaan myös uusi LevelController-olio, joka sisältää simulaation tasot
     */
    public CustomEngine(IControllerForM controller, String levelKey) {
        super(controller);

        levelController = new LevelController(controller, eventList);
        currentLevel = levelController.getLevel(levelKey);
    }

    /**
     * Metodi, joka alustaa simulaation.
     * Kutsutaan level-luokan startSimulation-metodia.
     */
    @Override
    protected void initializations() {
        currentLevel.startSimulation();
    }

    /**
     * @param event Tapahtuma, jota suoritetaan
     * Metodi, joka vastaa tapahtumien suorittamisesta
     * Metodi ensin tarkistaa, onko tapahtuma saapumistapahtuma, ja jos on, luodaan uusi asiakas ja lisätään asiakas ensimmäisen palvelupisteen jonoon.
     * Jos nykyisessä tasossa on tapahtumaa vastaava palvelupiste, poistetaan asiakas jonosta ja lisätään asiakas seuraavan palvelupisteen jonoon.
     * Jos seuraava palvelupiste on Roundabout, tarkistetaan, onko asiakkaalla määritetty Roundaboutin poistumisreitti.
     * Jos asiakkaalla ei ole seuraavaa palvelupistettä, asiakas poistuu simulaatiosta.
     *
     * Jos tapahtuma on Light Switch tai Road Crossing tapahtuma, vaihdetaan liikennevalon tai tienylityspaikan tila.
     */
    @Override
    protected void executeEvent(Event event) {  // B-vaiheen tapahtumat
        Customer selectedCustomer;
        String type = event.getType();
        if (currentLevel.isArrivalProcess(type)) {
            ArrivalProcess arrivalProcess = currentLevel.getArrivalProcess(type);
            Customer customer = new Customer(currentLevel, arrivalProcess.getX(), arrivalProcess.getY());
            currentLevel.getNextServicePoint(arrivalProcess).addToQueue(customer);
            controller.addCustomerToRendererQueue(customer);
            arrivalProcess.generateNext();
        } else if(currentLevel.hasNextServicePoint(type)) {
            ServicePoint servicePoint = currentLevel.getServicePoint(type);
            selectedCustomer = servicePoint.takeFromQueue();
            if (servicePoint.getClass() == Roundabout.class) {
                if (!selectedCustomer.getRoundaboutExit().equals(type)) currentLevel.getNextRoundaboutServicePoint(servicePoint, false).addToQueue(selectedCustomer);
                else if (currentLevel.getNextServicePointCount(type) > 1) {
                    selectedCustomer.setRoundaboutExit(null);
                    currentLevel.getNextRoundaboutServicePoint(servicePoint, true).addToQueue(selectedCustomer);
                } else {
                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                    selectedCustomer.report();
                }
            } else {
                currentLevel.getNextServicePoint(servicePoint).addToQueue(selectedCustomer);
            }
        } else {
            if(type.contains("Light Switch")) {
                ((TrafficLights) currentLevel.getServicePoint(type.replace(" Light Switch", ""))).switchGreenLight();
            } else if(type.contains("Road Crossing")) {
                ((Crosswalk) currentLevel.getServicePoint(type.replace(" Road Crossing", ""))).switchCrossable();
            } else {
                ServicePoint servicePoint = currentLevel.getServicePoint(type);
                selectedCustomer = servicePoint.takeFromQueue();
                selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                selectedCustomer.report();
            }
        }
    }

    /**
     * Metodi C-tapahtumien suorittamiselle.
     * Metodi käy läpi kaikki nykyisen tason palvelupisteet ja tarkistaa, onko palvelupiste varattu tai onko palvelupisteessä asiakkaita jonossa.
     * Jos palvelupiste on varattu tai palvelupisteessä ei ole asiakkaita jonossa, metodi jatkaa seuraavaan palvelupisteeseen.
     * Jos palvelupiste on liikennevalo, tarkistetaan, onko liikennevalo vihreä.
     * Jos liikennevalo on vihreä, tarkistetaan ehtiikö asiakas siirtymään liikennevalojen läpi ennen seuraavaa valojen vaihtumista.
     * Jos asiakas ehtii siirtymään liikennevalojen läpi, asiakas poistuu jonosta ja siirtyy seuraavaan palvelupisteeseen.
     * Jos palvelupiste on tienylityspaikka, tarkistetaan, onko tienylityspaikka ylitettävissä.
     * Jos tienylityspaikka on ylitettävissä, tarkistetaan ehtiikö asiakas ylittämään tienylityspaikan ennen seuraavaa tienylitystapahtumaa.
     * Jos asiakas ehtii ylittämään tienylityspaikan, asiakas poistuu jonosta ja siirtyy seuraavaan palvelupisteeseen.
     */
    @Override
    protected void tryCEvents() {
        for (ServicePoint servicePoint : currentLevel.getServicePoints()) {
            try{
            if (servicePoint.isReserved() || !servicePoint.queueNotEmpty()) continue;

            if(servicePoint.getClass() != Roundabout.class && currentLevel.hasNextServicePoint(servicePoint) && currentLevel.getNextServicePoint(servicePoint).getClass() == Roundabout.class) {
                if (!currentLevel.getNextServicePoint(servicePoint).queueNotEmpty()) servicePoint.startService();
            }
            else if (servicePoint.getClass() == TrafficLights.class) {
                TrafficLights trafficPoint = (TrafficLights)servicePoint;
                if(!trafficPoint.isGreenLight()) continue;
                if(ServicePoint.getCarSpacingInterval() + this.currentTime() < trafficPoint.getNextLightSwitchEvent().getTime()) {
                    trafficPoint.startService();
                }
            }
            else if (servicePoint.getClass() == Crosswalk.class) {
                Crosswalk crosswalk = (Crosswalk)servicePoint;
                if(!crosswalk.isCrossable()) continue;
                if(crosswalk.generateSampleDelay() + this.currentTime() < crosswalk.getNextCrossingEvent().getTime()) {
                    crosswalk.startService();
                }
            }

            else servicePoint.startService();}
            catch (Exception e) {
                System.out.println("ServicePoint: "+servicePoint.getScheduledEventType()+" next servicePoint unknown.");
                throw e;
            }
        }
    }

    /**
     * Metodi joka tulostaa simulaation päättymisajan konsoliin simulaation päätyttyä.
     */
    @Override
    protected void results() {
        System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
    }

    public LevelController getLevelController() {
        return levelController;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }
}
