package simu.framework;
import eduni.distributions.*;
import lib.Rounding;

/**
 * Tämä on Saapumisprosessi luokka jossa määritellään saapumisprosessin logiikka.
 */
public class ArrivalProcess {
	/**
	 * @param generator on ArrivalProcess luokan yksi ominaisuus joka ottaa ContinuousGenerator olion itselleen tällä saadaan asetettua haluttu jakauma.
	 */
	private ContinuousGenerator generator;
	/**
	 * @param eventList on ArrivalProcess luokan yksi ominaisuus joka ottaa EventList olion itselleen tällä saadaan asetettua haluttuja tapahtumia tapahtumalistaan.
	 */
	private EventList eventList;
	/**
	 * @param type on ArrivalProcess luokan yksi ominaisuus joka ottaa String olion itselleen tällä saadaan asetettua haluttu tapahtuman tyyppi.
	 */
	private String type;
	/**
	 * @param x on ArrivalProcess luokan yksi ominaisuus joka ottaa double olion itselleen tällä saadaan asetettua haluttu x locaatio misin asiakas ilmestyy.
	 */
	private double x;
	/**
	 * @param y on ArrivalProcess luokan yksi ominaisuus joka ottaa double olion itselleen tällä saadaan asetettua haluttu y locaatio misin asiakas ilmestyy.
	 */
	private double y;

	/**
	 * @param g ominaisuus joka ottaa ContinuousGenerator olion itselleen tällä saadaan asetettua haluttu jakauma.
	 * @param el ominaisuus joka ottaa EventList olion itselleen tällä saadaan asetettua haluttuja tapahtumia tapahtumalistaan.
	 * @param type ominaisuus joka ottaa String olion itselleen tällä saadaan asetettua haluttu tapahtuman tyyppi.
	 */
	public ArrivalProcess(ContinuousGenerator g, EventList el, String type) {
		this.generator = g;
		this.eventList = el;
		this.type = type;
	}

	/**
	 * @param g ominaisuus joka ottaa ContinuousGenerator olion itselleen tällä saadaan asetettua haluttu jakauma.
	 * @param el ominaisuus joka ottaa EventList olion itselleen tällä saadaan asetettua haluttuja tapahtumia tapahtumalistaan.
	 * @param type ominaisuus joka ottaa String olion itselleen tällä saadaan asetettua haluttu tapahtuman tyyppi.
	 * @param x ominaisuus joka ottaa double olion itselleen tällä saadaan asetettua haluttu x locaatio misin asiakas ilmestyy.
	 * @param y ominaisuus joka ottaa double olion itselleen tällä saadaan asetettua haluttu y locaatio misin asiakas ilmestyy.
	 */
	public ArrivalProcess(ContinuousGenerator g, EventList el, String type, double x, double y) {
		this.generator = g;
		this.eventList = el;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return palauttaa seuraavan tapahtuman.
	 */
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
