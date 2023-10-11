package simu.framework;
import eduni.distributions.*;
import lib.Rounding;

public class ArrivalProcess {
	private ContinuousGenerator generator;
	private EventList eventList;
	private String type;
	private double x, y;

	public ArrivalProcess(ContinuousGenerator g, EventList el, String type) {
		this.generator = g;
		this.eventList = el;
		this.type = type;
	}

	public ArrivalProcess(ContinuousGenerator g, EventList el, String type, double x, double y) {
		this.generator = g;
		this.eventList = el;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public Event generateNext() {
		double sample = generator.sample();
		Event event = new Event(type, Clock.getInstance().getTime() + sample);
		eventList.add(event);
		return event;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getScheduledEventType() {
		return type;
	}

	public void setGenerator(ContinuousGenerator generator) {
		this.generator = generator;
	}
}
