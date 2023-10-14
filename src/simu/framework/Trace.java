package simu.framework;

/**
 * Trace-luokka, joka sisältää enumeja ja metodeja konsoliin tulevia printtejä varten.
 */
public class Trace {
	/**
	 * Level enum, joka sisältää eri tasoja.
	 * Level.INFO printtaa konsoliin kaikki printit.
	 * Level.WAR printtaa konsoliin vain varoitukset ja virheet.
	 * Level.ERR printtaa konsoliin vain virheet.
	 */
	public enum Level{INFO, WAR, ERR}

	/**
	 * Metodi, joka asettaa Trace-tason.
	 */
	private static Level traceLevel = Level.INFO;

	/**
	 * @param lvl Level enum, joka asettaa Trace-tason.
	 * @param txt String, joka printataan konsoliin.
	 */
	public static void out(Level lvl, String txt) {
		if (lvl.ordinal() >= traceLevel.ordinal()) {
			System.out.println(txt);
		}
	}

	public static void setTraceLevel(Level lvl) {
		traceLevel = lvl;
	}
}