package testi;

import simu.framework.Engine;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.CustomEngine;

public class Simulator { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);
		Engine m = new CustomEngine();
		m.setSimulationTime(1000);
		m.run();
		///
	}
}
