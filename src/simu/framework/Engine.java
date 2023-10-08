package simu.framework;

import controller.IControllerForM;
import simu.model.CustomEngine;

public abstract class Engine extends Thread implements IEngine {
	private double simulationTime = 0;
	static private int engineCount = 0;
	private int engineNumber = 0;
	private long delay = 0;
	private Clock clock;
	protected EventList eventList;
	protected IControllerForM controller;

	public Engine(IControllerForM controller) {
		this.controller = controller;
		clock = Clock.getInstance(); // Otetaan clock muuttujaan yksinkertaistamaan koodia
		clock.setTime(0);
		eventList = new EventList();
		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa 
		this.engineNumber = ++engineCount;
	}

	public void setDelay(long time) {
		delay = time;
	}

	public long getDelay() {
		return (long) delay;
	}
	public void setSimulationTime(double time) {
		simulationTime = time;
	}

	public void run() {
		initializations(); // luodaan mm. ensimmäinen tapahtuma
		while (simulating()) {
			delay();

			Trace.out(Trace.Level.INFO, "\nA-vaihe: clock on " + currentTime());
			clock.setTime(currentTime());
			
			Trace.out(Trace.Level.INFO, "\nB-vaihe:" );
			executeBEvents();
			
			Trace.out(Trace.Level.INFO, "\nC-vaihe:" );
			tryCEvents();

			this.controller.showEndtime(this.clock.getTime());
		}
		results();
		controller.uploadResults(((CustomEngine) controller.getEngine()).getCurrentLevel());
		controller.enableStartButton();
	}
	
	private void executeBEvents() {
		while (eventList.getNextTime() == clock.getTime()) {
			executeEvent(eventList.delete());
		}
	}

	protected double currentTime() {
		return eventList.getNextTime();
	}
	
	public boolean simulating() {
		return clock.getTime() < simulationTime && this.engineNumber == engineCount;
	}

	protected abstract void executeEvent(Event t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void tryCEvents();	// Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void initializations(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void results(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	private void delay() { // UUSI
		Trace.out(Trace.Level.INFO, "Viive " + delay);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Clock getClock() {
		return clock;
	}
}