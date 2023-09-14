package simu.model;

import simu.framework.*;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
class ServicePoint {

	protected final LinkedList<Customer> queue = new LinkedList<>(); // Tietorakennetoteutus
	protected final ContinuousGenerator generator;
	protected final EventList eventList;
	protected final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys

	protected boolean reserved = false;

	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType tyyppi) {
		this.eventList = eventList;
		this.generator = generator;
		this.scheduledEventType = tyyppi;
	}


	public void addToQueue(Customer a) {   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}


	public Customer takeFromQueue() {  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll();
	}


	public void startService() {  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
		
		reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime));
	}



	public boolean isReserved() {
		return reserved;
	}



	public boolean queueNotEmpty() {
		return queue.size() != 0;
	}

}
