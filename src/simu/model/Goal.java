package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

public class Goal extends ServicePoint {
    public Goal(EventList eventList, String type) {
        super(eventList, type);
    }

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        queue.peek().setFirstInQueue(true);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + 2));
    }

    @Override
    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.addDestination(this.getX() + (Math.random() - 0.5), this.getY() + (Math.random() - 0.5));
        customer.setFirstInQueue(false);
    }
}
