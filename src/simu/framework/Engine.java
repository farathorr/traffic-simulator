package simu.framework;

public abstract class Engine {
	
	private double simulationTime = 0;
	
	private Clock clock;
	
	protected EventList eventList;

	public Engine() {

		clock = Clock.getInstance(); // Otetaan clock muuttujaan yksinkertaistamaan koodia
		
		eventList = new EventList();
		
		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa 
		
		
	}

	public void setSimulationTime(double time) {
		simulationTime = time;
	}
	
	
	public void run() {
		initializations(); // luodaan mm. ensimmäinen tapahtuma
		while (simulating()) {
			
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
	
}