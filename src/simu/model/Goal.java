package simu.model;

import lib.Rounding;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;

public class Goal extends ServicePoint {
    public Goal(EventList eventList, String type) {
        super(eventList, type);
    }

    private double totalTime;
    private int carCount = 0;

    @Override
    public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
        reserved = true;
        Customer customer = queue.peek();
        carCount++;
        totalTime += Clock.getInstance().getTime() - customer.getArrivalTime();
        customer.setFirstInQueue(true);
        eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + 2));
    }

    @Override
    public void addToQueue(Customer customer) {
        queue.add(customer);
        customer.addDestination(this.getX() + (Math.random() - 0.5), this.getY() + (Math.random() - 0.5));
        customer.setFirstInQueue(false);
    }

    public double getAverageCompletionTime() {
        if(carCount == 0) return 0;
        System.out.println("carCount: " + carCount);
        System.out.println("totalTime: " + totalTime);
        return Rounding.toFixed((totalTime/carCount),2);
    }

    public int getCarCount() {
        return carCount;
    }
}
