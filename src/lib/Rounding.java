package lib;

/**
 * Numeroitten pyöristys kirjasto
 */
public class Rounding {

    private Rounding() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Pyöristää double luvun haluttuun desimaaliin
     * @param value double, joka pyöristetään
     * @param precision montako desimaalia jätetään
     * @return pyöristetty double
     */
    public static double toFixed(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
