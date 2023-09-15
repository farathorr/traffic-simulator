package simu.model;

import simu.framework.*;
import eduni.distributions.Normal;

public class CustomEngine extends Engine {
	
	private ArrivalProcess arrivalProcess;

	private ServicePoint[] servicePoints;

	public CustomEngine() {
		servicePoints = new ServicePoint[3];
		servicePoints[0] = new ServicePoint(new Normal(10,6), eventList, EventType.DEP1);
		servicePoints[1] = new ServicePoint(new Normal(10,10), eventList, EventType.DEP2);
		servicePoints[2] = new TrafficLights(new Normal(5,3), eventList);

		// arrivalProcess = new ArrivalProcess(new Negexp(15,5), eventList, EventType.ARR1);
		arrivalProcess = new ArrivalProcess(new Normal(15,5), eventList, EventType.ARR2);
	}


	@Override
	protected void initializations() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t) {  // B-vaiheen tapahtumat
		Customer selectedCustomer;
        switch ((EventType) t.getType()) {
            case ARR1 -> {
                servicePoints[0].addToQueue(new Customer());
                arrivalProcess.generateNext();
            }
			case ARR2 -> {
				servicePoints[2].addToQueue(new Customer());
				arrivalProcess.generateNext();
			}
			case LIGHT_SWITCH -> {
				((TrafficLights)servicePoints[2]).switchLight();
				((TrafficLights)servicePoints[2]).generateNextEvent();
			}
            case DEP1 -> {
				selectedCustomer = servicePoints[0].takeFromQueue();
                servicePoints[1].addToQueue(selectedCustomer);
            }
            case DEP2 -> {
				selectedCustomer = servicePoints[1].takeFromQueue();
                servicePoints[2].addToQueue(selectedCustomer);
            }
			case TRAFFIC_LIGHTS -> {
				selectedCustomer = servicePoints[2].takeFromQueue();
				selectedCustomer.setLeavingTime(Clock.getInstance().getTime());
				selectedCustomer.report();
            }
        }
	}

	@Override
	protected void tryCEvents() {
		for (ServicePoint servicePoint: servicePoints) {
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
