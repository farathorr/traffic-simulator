package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.EventList;

public class Crosswalk extends ServicePoint{

    public Crosswalk(ContinuousGenerator generator, EventList eventList, EventType tyyppi) {
        super(generator, eventList, tyyppi);
    }
}
