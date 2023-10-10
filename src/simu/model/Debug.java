package simu.model;

import simu.framework.Clock;

public class Debug {
    private static Debug debug = null;
    private static boolean debugMode = false;

    private Debug() {

    }

    public static void setDebug(boolean debug) {
        debugMode = debug;
    }

    public static Debug getInstance() {
        if (debug == null) {
            debug = new Debug();
        }
        return debug;
    }

    public boolean isDebug() {
        return debugMode;
    }
}
