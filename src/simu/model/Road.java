package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

public class Road extends ServicePoint {
    public Road(ContinuousGenerator generator, EventList eventList, String type) {
        super(generator, eventList, type);
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        queue.peek().setFirstInQueue(true);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + 2));
    }
}
