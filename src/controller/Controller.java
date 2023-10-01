package controller;

import javafx.application.Platform;
import simu.framework.Engine;
import simu.framework.IEngine;
import simu.model.CustomEngine;
import simu.model.Customer;
import simu.model.Level;
import simu.model.ServicePoint;
import view.ISimulatorUI;
import view.RenderLoop;

public class Controller implements IControllerForM, IControllerForV {
	
	private IEngine engine;
	private ISimulatorUI ui;
	
	public Controller(ISimulatorUI ui) {
		this.ui = ui;
	}

	@Override
	public void startSimulator() {
		engine = new CustomEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		Customer.resetCustomerCount();
		engine.getClock().setTime(0);
		engine.setSimulationTime(ui.getTime());
		engine.setDelay(ui.getDelay());
		ui.getVisualization().clearScreen();
		ui.getRenderLoop().start();
		((Thread) engine).start();
	}
	
	@Override
	public void slowdown() { // hidastetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*1.5));
	}

	@Override
	public void speedup() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
	}
	
	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen:
		
	@Override
	public void showEndtime(double time) {
		Platform.runLater(() -> ui.setEndTime(time));
	}

	@Override
	public void render(Level level, String type, double x, double y, String rotation) {
		ServicePoint servicePoint = level.getServicePoint(type);
		servicePoint.render(x, y, rotation);
		ui.getVisualization().addToRenderQueue(servicePoint);
//		Platform.runLater(new Runnable() {
//			public void run(){
//				ui.getVisualization().render(level, type, x, y, rotation);
//			}
//		});
	}

	public Engine getEngine() {
		return (Engine) engine;
	}
}
