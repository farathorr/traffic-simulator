package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

/**
 * Tie-palvelupisteen luokka.
 * Luokka perii ServicePoint-luokan.
 */
public class Road extends ServicePoint {
    /**
     * Tie-palvelupisteen suurin jonon pituus.
     */
    private int maxQueueSize = 0;
    /**
     * @param eventList EventList-olio, joka sisältää simulaation tapahtumalistan
     * @param type Palvelupisteen tyyppi.
     */
    public Road(EventList eventList, String type) {
        super(eventList, type);
    }

    /**
     * Metodi joka käynnistää palvelun.
     * Palvelupiste on varattu palvelun aikana.
     * Asiakas on jonossa palvelun aikana.
     * Tapahtumalistaan lisätään uusi tapahtuma, joka on palvelun päättymisen tapahtuma.
     * Jonon maksimipituus päivitetään, jos maksimi ylittyy.
     */
    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        queue.peek().setFirstInQueue(true);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + 2));
        if(this.maxQueueSize < this.getQueue().size()){
            this.maxQueueSize = this.getQueue().size();
        }
    }

    @Override
    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}
