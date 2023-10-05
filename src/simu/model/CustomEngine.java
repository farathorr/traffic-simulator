package simu.model;

import controller.IControllerForM;
import simu.framework.*;
import eduni.distributions.Normal;

public class CustomEngine extends Engine {
    private Level currentLevel;

    public CustomEngine(IControllerForM controller) {
        super(controller);

        LevelController levelController = new LevelController(controller, eventList);
        currentLevel = levelController.getLevel(3);
    }

    @Override
    protected void initializations() {
        currentLevel.startSimulation();
    }

    @Override
    protected void executeEvent(Event event) {  // B-vaiheen tapahtumat
        Customer selectedCustomer;
        String type = event.getType();
        if (currentLevel.isArrivalProcess(type)) {
            ArrivalProcess arrivalProcess = currentLevel.getArrivalProcess(type);
            Customer customer = new Customer(currentLevel, arrivalProcess.getX(), arrivalProcess.getY());
            currentLevel.getNextServicePoint(arrivalProcess).addToQueue(customer);
            controller.addCustomerToRendererQueue(customer);
            arrivalProcess.generateNext();
        } else if(currentLevel.hasNextServicePoint(type)) {
            ServicePoint servicePoint = currentLevel.getServicePoint(type);
            selectedCustomer = servicePoint.takeFromQueue();
            if (servicePoint.getClass() == Roundabout.class) {
                if (!selectedCustomer.getRoundaboutExit().equals(type)) currentLevel.getNextRoundaboutServicePoint(servicePoint, false).addToQueue(selectedCustomer);
                else if (currentLevel.getNextServicePointCount(type) > 1) {
                    selectedCustomer.setRoundaboutExit(null);
                    currentLevel.getNextRoundaboutServicePoint(servicePoint, true).addToQueue(selectedCustomer);
                } else {
                    selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                    selectedCustomer.report();
                }
            } else {
                currentLevel.getNextServicePoint(servicePoint).addToQueue(selectedCustomer);
            }
        } else {
            if(type.contains("Light Switch")) {
                ((TrafficLights) currentLevel.getServicePoint(type.replace(" Light Switch", ""))).switchGreenLight();
            } else if(type.contains("Road Crossing")) {
                ((Crosswalk) currentLevel.getServicePoint(type.replace(" Road Crossing", ""))).switchCrossable();
            } else {
                ServicePoint servicePoint = currentLevel.getServicePoint(type);
                selectedCustomer = servicePoint.takeFromQueue();
                selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
                selectedCustomer.report();
            }
        }
    }

    @Override
    protected void tryCEvents() {
        for (ServicePoint servicePoint : currentLevel.getServicePoints()) {
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
