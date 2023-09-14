package testi;

import lib.Rounding;
import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.CustomEngine;

public class Simulator { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);
		Engine customEngine = new CustomEngine();
		customEngine.setSimulationTime(50);
		customEngine.run();
	}
}
