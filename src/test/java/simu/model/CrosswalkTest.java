package simu.model;

import org.junit.jupiter.api.Test;
import simu.framework.EventList;

import static org.junit.jupiter.api.Assertions.*;

class CrosswalkTest {

    @Test
    void reservedTest() {
        EventList eventList = new EventList();
        Crosswalk crosswalk = new Crosswalk(1, 1, 1, 1, eventList, "test");
        crosswalk.addToQueue(new Customer(null, 1, 1));
        crosswalk.startService();
        assertTrue(crosswalk.isReserved());
        Customer selectedCustomer = crosswalk.takeFromQueue();
        assertFalse(crosswalk.isReserved());
        assertFalse(crosswalk.getQueue().contains(selectedCustomer));
    }

    @Test
    void CrosswalkEventListTest() {
        EventList eventList = new EventList();
        Level testLevel = new Level("trafficLightsTest");
        Crosswalk crosswalk = new Crosswalk(1, 1, 1, 1, eventList, "test");
        testLevel.add(crosswalk);
        crosswalk.init();
        assertTrue(eventList.getList().contains(crosswalk.getNextCrossingEvent()));
    }

    @Test
    void isCrossable() {
        EventList evenList = new EventList();
        Level level = new Level("test");
        Crosswalk crosswalk = new Crosswalk(5, 5, 5, 5, evenList, "crosswalk");
        level.add(crosswalk);
        assertTrue(crosswalk.isCrossable());
        crosswalk.init();
        assertTrue(crosswalk.isCrossable());
        crosswalk.switchCrossable();
        assertFalse(crosswalk.isCrossable());
    }
}