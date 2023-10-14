package simu.model;

import org.junit.jupiter.api.Test;
import simu.framework.EventList;

import static org.junit.jupiter.api.Assertions.*;

class TrafficLightsTest {

    @Test
    void reservedTest() {
        EventList eventList = new EventList();
        TrafficLights trafficLights = new TrafficLights(1, 1, 1, 1, eventList, "test");
        trafficLights.addToQueue(new Customer(null, 1, 1));
        trafficLights.startService();
        assertTrue(trafficLights.isReserved());
    }

    @Test
    void lightSwitchEventTest() {
        EventList eventList = new EventList();
        Level testLevel = new Level("trafficLightsTest");
        TrafficLights trafficLights = new TrafficLights(1, 1, 1, 1, eventList, "test");
        testLevel.add(trafficLights);
        trafficLights.init();
        assertTrue(eventList.getList().contains(trafficLights.getNextLightSwitchEvent()));
    }

}