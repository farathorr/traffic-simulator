package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

public class Intersection extends ServicePoint {


    public Intersection(ContinuousGenerator generator, EventList eventList, EventType tyyppi) {
        super(generator, eventList, tyyppi);
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        int randInt = (int) ((Math.random()) * 100);
        if (randInt > 50) {
            Trace.out(Trace.Level.INFO, "Auto kääntyy oikealle.");
            eventList.add(new Event(EventType.TURN_RIGHT, Clock.getInstance().getTime()));
        }
        else{
            Trace.out(Trace.Level.INFO, "Auto kääntyy vasemmalle.");
            eventList.add(new Event(EventType.TURN_LEFT, Clock.getInstance().getTime()));
        }
    }


}
