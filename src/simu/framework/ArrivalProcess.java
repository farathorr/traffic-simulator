package simu.framework;
import eduni.distributions.*;
import lib.Rounding;

public class ArrivalProcess {
	
	private ContinuousGenerator generator;
	private EventList eventList;
	private IEventType type;

	public ArrivalProcess(ContinuousGenerator g, EventList el, IEventType type) {
		this.generator = g;
		this.eventList = el;
		this.type = type;
	}

	public void generateNext() {
		double sample = generator.sample();
		Event e = new Event(type, Clock.getInstance().getTime() + sample);
		eventList.add(e);
	}

}
