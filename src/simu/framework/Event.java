package simu.framework;

import lib.Rounding;

/**
 * Event-luokka, joka sisältää tapahtuman tyypin ja tapahtuma-ajan.
 * Event-luokka implementoi Comparable-rajapinnan, joka mahdollistaa Event-olioiden vertailun.
 * Event-oliot ovat järjestetty ajan mukaan.
 */
public class Event implements Comparable<Event> {
	/**
	 *
	 */
	private String type;
	/**
	 * Tapahtuma-aika.
	 */
	private double time;

	/**
	 * @param type Tapahtuman tyyppi.
	 * @param time Tapahtuma-aika.
	 */
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

	/**
	 * @param arg Event-olio, johon verrataan.
	 * @return Palauttaa -1, jos this.time < arg.time.
	 * Palauttaa 1, jos this.time > arg.time.
	 * Palauttaa 0, jos this.time == arg.time.
	 */
	@Override
	public int compareTo(Event arg) {
		if (this.time < arg.time) return -1;
		else if (this.time > arg.time) return 1;
		return 0;
	}

}
