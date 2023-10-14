package simu.framework;

import controller.IControllerForM;
import simu.model.CustomEngine;
import simu.model.Customer;

/**
 * Tämä on Moottori luokka jossa määritellään moottorin logiikka.
 * Moottori on abstrakti luokka joka perii Thread luokan.
 * Moottori toteuttaa IEngine rajapinnan.
 */
public abstract class Engine extends Thread implements IEngine {
	/**
	 * Moottorin yksi ominaisuus joka ottaa simulaatioajan itselleen.
	 */
	private double simulationTime = 0;
	/**
	 * Moottorin yksi ominaisuus joka ottaa moottorin numeron itselleen.
	 * Tämä ominaisuus on staattinen jotta saadaan laskettua montako moottoria on luotu.
	 */
	static private int engineCount = 0;
	/**
	 * Moottorin yksi ominaisuus joka ottaa moottorin numeron itselleen.
	 */
	private int engineNumber = 0;
	/**
	 * Moottorin yksi ominaisuus joka ottaa viiveen itselleen.
	 */
	private long delay = 0;
	/**
	 * Moottorin yksi ominaisuus joka ottaa kellon itselleen.
	 */
	private Clock clock;
	/**
	 * Moottorin yksi ominaisuus joka ottaa tapahtumalistan itselleen.
	 */
	protected EventList eventList;
	/**
	 * Moottorin yksi ominaisuus joka ottaa kontrollerin itselleen rajapinnasta.
	 */
	protected IControllerForM controller;

	/**
	 * Moottorin konstruktori joka ottaa kontrollerin itselleen rajapinnasta.
	 * controller on IControllerForM rajapinnan olio.
	 * Tämä olio on kontrolleri joka on yhteydessä Moottoriin.
	 * Kellolle asetetaan nolla aika.
	 * Lisätään moottorin määrä yhdellä.
	 */
	public Engine(IControllerForM controller) {
		this.controller = controller;
		clock = Clock.getInstance();
		clock.setTime(0);
		eventList = new EventList();
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

	/**
	 * Tämä metodi on Moottorin run metodi jossa määritellään moottorin toiminta.
	 * Moottori käy läpi A, B ja C vaiheet.
	 * A-vaiheessa asetetaan kello aika.
	 * B-vaiheessa suoritetaan B tapahtumat.
	 * C-vaiheessa suoritetaan C tapahtumat.
	 * Kun moottori on suorittanut kaikki vaiheet niin se lataa tulokset.
	 */
	public void run() {
		initializations();
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
		controller.uploadResults(((CustomEngine) controller.getEngine()).getCurrentLevel());
		controller.enableStartButton();
	}

	/**
	 * Tämä metodi suorittaa B tapahtumat.
	 */
	private void executeBEvents() {
		while (eventList.getNextTime() == clock.getTime()) {
			executeEvent(eventList.delete());
		}
	}

	protected double currentTime() {
		return eventList.getNextTime();
	}

	/**
	 * @return Palauttaa true jos kello aika on pienempi kuin simulaatioaika ja moottorin numero on sama kuin moottorien määrä.
	 */
	public boolean simulating() {
		return clock.getTime() < simulationTime && this.engineNumber == engineCount;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	protected abstract void executeEvent(Event t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void tryCEvents();	// Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void initializations(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void results(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	/**
	 *
	 */
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