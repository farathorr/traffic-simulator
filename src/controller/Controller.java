package controller;

import javafx.application.Platform;
import simu.dao.Level_variableDao;
import simu.dao.ResultsDao;
import simu.entity.Level_variables;
import simu.entity.Results;
import simu.framework.Engine;
import simu.framework.IEngine;
import simu.model.*;
import view.ISimulatorUI;

import java.util.List;

public class Controller implements IControllerForM, IControllerForV {
	
	private IEngine engine;
	private ISimulatorUI ui;
	private String levelKey = "DEBUG world";

	private Level_variableDao levelvariableDao = new Level_variableDao();

	private ResultsDao resultsDao = new ResultsDao();
	
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

	public void uploadResults(Level level) {
		Results results = new Results(5, 0.5, 0.5, level.getLevelName());
		resultsDao.persist(results);
		for (ServicePoint servicePoint : level.getServicePoints()) {
			Level_variables levelVariables = new Level_variables(results, servicePoint.getScheduledEventType(), 0.5, 0.5);
			levelvariableDao.persist(levelVariables);
		}
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

	public LevelSettings getLevelSettings() {
		return LevelSettings.getInstance();
	}

	@Override
	public List<Results> getResults(){
		return resultsDao.getAll();
	}
}
