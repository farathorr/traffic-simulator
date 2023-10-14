package simu.model;

import simu.framework.Clock;

/**
 * Debug luokka joka sisältää GUI:n debug-tilaan liittyviä muuttujia.
 */
public class Debug {
    /**
     * Debug luokan instanssi.
     */
    private static Debug debug = null;
    /**
     * Debug-tilan boolean-muuttuja.
     */
    private static boolean debugMode = false;
    /**
     * Grid-tilan boolean-muuttuja.
     */
    private boolean gridMode = false;

    /**
     * Debug luokan private-konstruktori.
     */
    private Debug() {

    }

    /**
     * @return Debug luokan instanssi.
     */
    public static Debug getInstance() {
        if (debug == null) {
            debug = new Debug();
        }
        return debug;
    }

    public boolean isDebug() {
        return debugMode;
    }

    public void toggle() {
        debugMode = !debugMode;
    }

    public void setGridMode(boolean gridMode) {
        this.gridMode = gridMode;
    }

    public boolean isGrid() {
        return gridMode;
    }
}
