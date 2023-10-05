package simu.model;

import controller.IControllerForM;
import simu.framework.*;
import eduni.distributions.Normal;

import java.util.List;

public class CustomEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private Level level1 = new Level();
    private ServicePoint[] servicePoints;

    public CustomEngine(IControllerForM controller) {
        super(controller);
        level1.arrival(new ArrivalProcess(new Normal(5, 5), eventList, "ARR1", 0, 0), "road0_0");
        level1.add(new Road(new Normal(5, 3), eventList, "road0_0"), "crosswalk1_0");
        level1.add(new Crosswalk(new Normal(1, 2), new Normal(50, 5), eventList, "crosswalk1_0"), "road1_1");
        level1.add(new Road(new Normal(5, 3), eventList, "road1_1"), "trafficlights1_2");
        level1.add(new TrafficLights(new Normal(2, 1), new Normal(50, 1), eventList, "trafficlights1_2"), "road1_3");
        level1.add(new Road(new Normal(5, 3), eventList, "road1_3"), new String[]{"road1_4", "road2_3"});
        level1.add(new Road(new Normal(5, 3), eventList, "road2_3"), "crosswalk3_3");
        level1.add(new Road(new Normal(5, 3), eventList, "road1_4"));
        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "crosswalk3_3"), "road4_3");
        level1.add(new Road(new Normal(5, 3), eventList, "road4_3"), "roundabout_right4_2");

        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_right4_2", 3), "roundabout_top5_2");
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_top5_2", 3), "roundabout_left5_1");
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_left5_1", 3), "roundabout_bottom4_1");
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "roundabout_bottom4_1", 3), "roundabout_right4_2");


        controller.render(level1, "road0_0", 0, 0, "right");
        controller.render(level1, "crosswalk1_0", 1, 0, "right");
        controller.render(level1, "road1_1", 1, 1, "bottom");
        controller.render(level1, "trafficlights1_2", 1, 2, "bottom");
        controller.render(level1, "road2_3", 2, 3, "right");
        controller.render(level1, "road1_3", 1, 3, "bottom");
        controller.render(level1, "road1_4", 1, 4, "bottom");
        controller.render(level1, "crosswalk3_3", 3, 3, "right");
        controller.render(level1, "road4_3", 4, 3, "right");
        controller.render(level1, "roundabout_right4_2", 4, 2, "right");
        controller.render(level1, "roundabout_top5_2", 5, 2, "top");
        controller.render(level1, "roundabout_left5_1", 5, 1, "left");
        controller.render(level1, "roundabout_bottom4_1", 4, 1, "bottom");



//
//        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_BOTTOM", 3), "ROUNDABOUT_RIGHT");
//        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_RIGHT", 3), "ROUNDABOUT_TOP");
//        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_TOP", 3), new String[]{"ROUNDABOUT_LEFT", "Intersection_vasen"});
//        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_LEFT", 3), "ROUNDABOUT_BOTTOM");
//
//        level1.add(new Road( new Normal(50, 50), eventList, "Intersection_vasen"));
//        level1.add(new Road( new Normal(50, 50), eventList, "Intersection_alas"));
//
//        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk_vasen"));
//        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk_alas"));
//
//        //controller.render(level1, "Intersection_vasen", 0, 0, "right");
//        controller.render(level1, "Intersection_alas", 1, 0, "bottom");
//
//        controller.render(level1, "Crosswalk_vasen", 2, 2, "top");
//        controller.render(level1, "Crosswalk_alas", 3, 2, "bottom");
//
//        controller.render(level1, "ROUNDABOUT_BOTTOM", 0, 1, "top");
//        controller.render(level1, "ROUNDABOUT_RIGHT", 1, 1, "bottom");
//        controller.render(level1, "ROUNDABOUT_TOP", 2, 1, "right");
//        controller.render(level1, "ROUNDABOUT_LEFT", 3, 1, "left");
//        level1.add(new Intersection( new Normal(50, 50), eventList, "Intersection_oikee"));
//       level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk"));
    }

    @Override
    protected void initializations() {
        level1.startSimulation();
        //arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
    }

    @Override
    protected void executeEvent(Event event) {  // B-vaiheen tapahtumat
        Customer selectedCustomer;
        String type = event.getType();
        if (level1.isArrivalProcess(type)) {
            ArrivalProcess arrivalProcess = level1.getArrivalProcess(type);
            Customer customer = new Customer(level1, arrivalProcess.getX(), arrivalProcess.getY());
            level1.getNextServicePoint(arrivalProcess).addToQueue(customer);
            controller.addCustomerToRendererQueue(customer);
            arrivalProcess.generateNext();
        } else if(level1.hasNextServicePoint(type)) {
            ServicePoint servicePoint = level1.getServicePoint(type);
            selectedCustomer = servicePoint.takeFromQueue();
            if (servicePoint.getClass() == Roundabout.class) {
                if (!selectedCustomer.getRoundaboutExit().equals(type)) level1.getNextRoundaboutServicePoint(servicePoint, false).addToQueue(selectedCustomer);
                else if (level1.getNextServicePointCount(type) > 1) {
                    selectedCustomer.setRoundaboutExit(null);
                    level1.getNextRoundaboutServicePoint(servicePoint, true).addToQueue(selectedCustomer);
                } else {
                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                    selectedCustomer.report();
                }
            } else {
                level1.getNextServicePoint(servicePoint).addToQueue(selectedCustomer);
            }
        } else {
            if(type.contains("Light Switch")) {
                ((TrafficLights) level1.getServicePoint(type.replace(" Light Switch", ""))).switchGreenLight();
            } else if(type.contains("Road Crossing")) {
                ((Crosswalk) level1.getServicePoint(type.replace(" Road Crossing", ""))).switchCrossable();
            } else {
                ServicePoint servicePoint = level1.getServicePoint(type);
                selectedCustomer = servicePoint.takeFromQueue();
                selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                selectedCustomer.report();
            }
        }
    }

    @Override
    protected void tryCEvents() {
        for (ServicePoint servicePoint : level1.getServicePoints()) {
            if (servicePoint.isReserved() || !servicePoint.queueNotEmpty()) continue;

            if (servicePoint.getClass() == TrafficLights.class) {
                TrafficLights trafficPoint = (TrafficLights)servicePoint;
                if(!trafficPoint.isGreenLight()) continue;
                if(trafficPoint.generateSampleDelay() + this.currentTime() < trafficPoint.getNextLightSwitchEvent().getTime()) {
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
            else servicePoint.startService();
        }
    }

    @Override
    protected void results() {
        System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
        System.out.println("Tulokset ... puuttuvat vielä");
    }
}
