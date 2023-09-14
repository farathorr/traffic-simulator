package lib;

public class Rounding {

    private Rounding() {
        throw new IllegalStateException("Utility class");
    }
    public static double toFixed(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
