package simu.model;

import controller.IControllerForM;
import simu.framework.*;
import eduni.distributions.Normal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomEngine extends Engine {
    private ArrivalProcess arrivalProcess;
    private Level level1 = new Level();
    private ServicePoint[] servicePoints;

    public CustomEngine(IControllerForM controller) {
        super(controller);
//        servicePoints = new ServicePoint[7];
//        servicePoints[0] = new Intersection(new Normal(50, 50), eventList, EventType.INTERSECTION);
//        servicePoints[1] = new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, EventType.CROSSWALK);
//        servicePoints[2] = new TrafficLights(new Normal(5, 3), new Normal(15, 1), eventList);
//        servicePoints[3] = new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, EventType.ROUNDABOUT_BOTTOM);
//        servicePoints[4] = new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, EventType.ROUNDABOUT_RIGHT);
//        servicePoints[5] = new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, EventType.ROUNDABOUT_TOP);
//        servicePoints[6] = new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, EventType.ROUNDABOUT_LEFT);
//        arrivalProcess = new ArrivalProcess(new Normal(15, 5), eventList, EventType.ARR1);


        level1.arrival(new ArrivalProcess(new Normal(15, 5), eventList, "ARR1"), "light");
        level1.add(new TrafficLights(new Normal(5, 3), new Normal(15, 1), eventList, "light"), "Crosswalk");
        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk"), "ROUNDABOUT_BOTTOM");

        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_BOTTOM", 3), "ROUNDABOUT_RIGHT");
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_RIGHT", 3), "ROUNDABOUT_TOP");
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_TOP", 3), new String[]{"ROUNDABOUT_LEFT", "Intersection_vasen"});
        level1.add(new Roundabout(new Normal(5, 1), new Normal(0, 3), eventList, "ROUNDABOUT_LEFT", 3), "ROUNDABOUT_BOTTOM");

        level1.add(new Intersection( new Normal(50, 50), eventList, "Intersection_vasen"));
        level1.add(new Intersection( new Normal(50, 50), eventList, "Intersection_alas"));

        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk_vasen"));
        level1.add(new Crosswalk(new Normal(5, 2), new Normal(10, 5), eventList, "Crosswalk_alas"));

        //controller.render(level1, "Intersection_vasen", 0, 0, "right");
        controller.render(level1, "Intersection_alas", 1, 0, "bottom");

        controller.render(level1, "Crosswalk_vasen", 2, 2, "top");
        controller.render(level1, "Crosswalk_alas", 3, 2, "bottom");

        controller.render(level1, "ROUNDABOUT_BOTTOM", 0, 1, "top");
        controller.render(level1, "ROUNDABOUT_RIGHT", 1, 1, "bottom");
        controller.render(level1, "ROUNDABOUT_TOP", 2, 1, "right");
        controller.render(level1, "ROUNDABOUT_LEFT", 3, 1, "left");
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
            level1.getNextServicePoint(arrivalProcess).addToQueue(new Customer(level1));
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
//        switch ((EventType) t.getType()) {
//            case ARR1 -> {
//                servicePoints[0].addToQueue(new Customer());
//                arrivalProcess.generateNext();
//            }
//            case TURN_LEFT -> {
//                selectedCustomer = servicePoints[0].takeFromQueue();
//                servicePoints[1].addToQueue(selectedCustomer);
//            }
//            case TURN_RIGHT -> {
//                selectedCustomer = servicePoints[0].takeFromQueue();
//                servicePoints[2].addToQueue(selectedCustomer);
//            }
//            case LIGHT_SWITCH -> ((TrafficLights) servicePoints[2]).switchGreenLight();
//            case TRAFFIC_LIGHTS -> {
//                selectedCustomer = servicePoints[2].takeFromQueue();
//                servicePoints[5].addToQueue(selectedCustomer);
//            }
//            case ROAD_CROSSING -> ((Crosswalk) servicePoints[1]).switchCrossable();
//            case CROSSWALK -> {
//                selectedCustomer = servicePoints[1].takeFromQueue();
//                servicePoints[3].addToQueue(selectedCustomer);
//            }
//            case ROUNDABOUT_BOTTOM -> {
//                selectedCustomer = servicePoints[3].takeFromQueue();
//                if (selectedCustomer.getRoundaboutExit() == EventType.ROUNDABOUT_BOTTOM) {
//                    selectedCustomer.setRoundaboutExit(null);
//                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
//                    selectedCustomer.report();
//                } else {
//                    servicePoints[4].addToQueue(selectedCustomer);
//                }
//            }
//            case ROUNDABOUT_RIGHT -> {
//                selectedCustomer = servicePoints[4].takeFromQueue();
//                if (selectedCustomer.getRoundaboutExit() == EventType.ROUNDABOUT_RIGHT) {
//                    selectedCustomer.setRoundaboutExit(null);
//                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
//                    selectedCustomer.report();
//                } else {
//                    servicePoints[5].addToQueue(selectedCustomer);
//                }
//            }
//            case ROUNDABOUT_TOP -> {
//                selectedCustomer = servicePoints[5].takeFromQueue();
//                if (selectedCustomer.getRoundaboutExit() == EventType.ROUNDABOUT_TOP) {
//                    selectedCustomer.setRoundaboutExit(null);
//                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
//                    selectedCustomer.report();
//                } else {
//                    servicePoints[6].addToQueue(selectedCustomer);
//                }
//            }
//            case ROUNDABOUT_LEFT -> {
//                selectedCustomer = servicePoints[6].takeFromQueue();
//                if (selectedCustomer.getRoundaboutExit() == EventType.ROUNDABOUT_LEFT) {
//                    selectedCustomer.setRoundaboutExit(null);
//                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
//                    selectedCustomer.report();
//                } else {
//                    servicePoints[3].addToQueue(selectedCustomer);
//                }
//            }
//        }
    }

    @Override
    protected void tryCEvents() {
        for (ServicePoint servicePoint : level1.getServicePoints()) {
            if (!servicePoint.isReserved() && servicePoint.queueNotEmpty()) {
                servicePoint.startService();
            }
        }
    }

    @Override
    protected void results() {
        System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
        System.out.println("Tulokset ... puuttuvat vielä");
    }
}
