package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;

public class Intersection extends ServicePoint {
    public Intersection(ContinuousGenerator generator, EventList eventList, String tyyppi) {
        super(generator, eventList, tyyppi);
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        if (this.generator.sample() > 50) {
            Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId() + " kääntyy oikealle.");
            eventList.add(new Event("TURN_RIGHT", Clock.getInstance().getTime() + 2));
        } else {
            Trace.out(Trace.Level.INFO, "Auto " + queue.peek().getId() + " kääntyy vasemmalle.");
            eventList.add(new Event("TURN_LEFT", Clock.getInstance().getTime() + 2));
        }
    }
}
