package simu.framework;

import controller.IControllerForM;

public abstract class Engine implements IEngine {
	private double simulationTime = 0;
	private long delay = 0;
	private Clock clock;
	protected EventList eventList;
	private double delay = 0;
	protected IControllerForM controller;

	public Engine(IControllerForM controller) {
		this.controller = controller;
		clock = Clock.getInstance(); // Otetaan clock muuttujaan yksinkertaistamaan koodia
		eventList = new EventList();
		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa 
		
		
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

		}
		results();
		
	}
	
	private void executeBEvents() {
		while (eventList.getNextTime() == clock.getTime()) {
			executeEvent(eventList.delete());
		}
	}

	private double currentTime() {
		return eventList.getNextTime();
	}
	
	private boolean simulating() {
		return clock.getTime() < simulationTime;
	}

	protected abstract void executeEvent(Event t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void tryCEvents();	// Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void initializations(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void results(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	private void delay() { // UUSI
		Trace.out(Trace.Level.INFO, "Viive " + delay);
		try {
			sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}