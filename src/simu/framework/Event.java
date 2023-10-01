package simu.framework;

import lib.Rounding;

public class Event implements Comparable<Event> {
	private String type;
	private double time;

	public Event(String type, double time) {
		this.type = type;
		this.time = Rounding.toFixed(time, 2);
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getTime() {
		return time;
	}

	@Override
	public int compareTo(Event arg) {
		if (this.time < arg.time) return -1;
		else if (this.time > arg.time) return 1;
		return 0;
	}

}
