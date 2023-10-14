package simu.framework;

import java.util.PriorityQueue;

/**
 * Tapahtumalista on prioriteettijono, joka sisältää Event-olioita.
 * Tapahtumalista on toteutettu PriorityQueue-rakenteella.
 */
public class EventList {
	/**
	 * PriorityQueue, joka sisältää Event-olioita.
	 * Event-oliot ovat järjestetty ajan mukaan.
	 */
	private PriorityQueue<Event> list = new PriorityQueue<Event>();
	
	public EventList() {
	 
	}

	/**
	 * @return Poistaa ja palauttaa ensimmäisen Event-olion.
	 */
	public Event delete() {
		Trace.out(Trace.Level.INFO,"Tapahtumalistasta poisto " + list.peek().getType() + " " + list.peek().getTime() );
		return list.remove();
	}

	/**
	 * @param t Lisää Event-olion tapahtumalistaan.
	 */
	public void add(Event t) {
		Trace.out(Trace.Level.INFO,"Tapahtumalistaan lisätään uusi " + t.getType() + " " + t.getTime());
		list.add(t);
	}

	/**
	 * @return Palauttaa ensimmäisen Event-olion tapahtuma-ajan.
	 */
	public double getNextTime() {
		return list.peek().getTime();
	}

	public PriorityQueue<Event> getList() {
		return list;
	}
}
