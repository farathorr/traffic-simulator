package simu.framework;

/**
 * Trace-luokka, joka sisältää enumeja ja metodeja konsoliin tulevia printtejä varten.
 */
public class Trace {
	/**
	 * Level enum, joka sisältää eri tasoja.
	 * Level.INFO 
	 */
	public enum Level{INFO, WAR, ERR}
	private static Level traceLevel = Level.INFO;
	public static void setTraceLevel(Level lvl) {
		traceLevel = lvl;
	}
	public static void out(Level lvl, String txt) {
		if (lvl.ordinal() >= traceLevel.ordinal()) {
			System.out.println(txt);
		}
	}
}