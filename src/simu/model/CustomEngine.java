package simu.model;

import simu.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class CustomEngine extends Engine {
	
	private ArrivalProcess arrivalProcess;

	private ServicePoint[] servicePoints;

	public CustomEngine(){

		servicePoints = new ServicePoint[3];

		servicePoints[0]=new ServicePoint(new Normal(10,6), eventList, EventType.DEP1);
		servicePoints[1]=new ServicePoint(new Normal(10,10), eventList, EventType.DEP2);
		servicePoints[2]=new ServicePoint(new Normal(5,3), eventList, EventType.DEP3);

		arrivalProcess = new ArrivalProcess(new Negexp(15,5), eventList, EventType.ARR1);

	}


	@Override
	protected void initializations() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat

		Customer a;
		switch ((EventType)t.getType()){

			case ARR1: servicePoints[0].addToQueue(new Customer());
				       arrivalProcess.generateNext();
				break;
			case DEP1: a = (Customer) servicePoints[0].takeFromQueue();
				   	   servicePoints[1].addToQueue(a);
				break;
			case DEP2: a = (Customer) servicePoints[1].takeFromQueue();
				   	   servicePoints[2].addToQueue(a);
				break;
			case DEP3:
				       a = (Customer) servicePoints[2].takeFromQueue();
					   a.setPoistumisaika(Clock.getInstance().getTime());
			           a.raportti();
		}
	}

	@Override
	protected void tryCEvents(){
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.queueNotEmpty()){
				p.startService();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}

	
}
