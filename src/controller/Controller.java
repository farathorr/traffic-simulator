package controller;

import javafx.application.Platform;
import simu.framework.Engine;
import simu.framework.IEngine;
import simu.model.CustomEngine;
import simu.model.Customer;
import simu.model.Level;
import simu.model.ServicePoint;
import view.ISimulatorUI;

public class Controller implements IControllerForM, IControllerForV {
	
	private IEngine engine;
	private ISimulatorUI ui;
	private String levelKey = "DEBUG world";
	
	public Controller(ISimulatorUI ui) {
		this.ui = ui;
		this.engine = new CustomEngine(this, levelKey);
	}

	@Override
	public void startSimulator() {
		ui.getVisualization().reset();
		Customer.resetCustomerCount();
		engine = new CustomEngine(this, levelKey); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(ui.getTime());
		engine.setDelay(ui.getDelay());
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
	}

	@Override
	public void addCustomerToRendererQueue(Customer customer) {
		ui.getVisualization().addToCustomerQueue(customer);
	}

	public Engine getEngine() {
		return (Engine) engine;
	}

	public void enableStartButton() {
		ui.enableSimulationSettings();
	}

	public void setLevelKey(String levelKey) {
		this.levelKey = levelKey;
	}
}
