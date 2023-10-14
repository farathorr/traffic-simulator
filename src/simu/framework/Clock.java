package simu.framework;

/**
 * Tämä on Kello luokka tätä käytetään ajan hakemiseen ja luomiseen. Kello on singleton luokka.
 */
public class Clock {
	/**
	 * Aika muuttuja joka on double tyyppinen. Tätä käytetään ajan hakemiseen ja asettamiseen.
	 */
	private double time;
	/**
	 * instance on Kello luokan yksi ominaisuus joka ottaa Kello olion itselleen tällä saadaan luotua singleton luokka.
	 */
	private static Clock instance;

	/**
	 * Tämä kellon konstruktori. Asettaa kellon ajan nollaksi.
	 */
	private Clock() {
		time = 0;
	}

	/**
	 * @return palauttaa kellon instanssin. Jos instanssia ei ole niin luo uuden instanssin.
	 */
	public static Clock getInstance() {
		if (instance == null) {
			instance = new Clock();
		}
		return instance;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getTime() {
		return time;
	}
}
