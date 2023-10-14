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

/**
 * Kontrolleri, joka välittää tietoa moottorille ja käyttöliittymälle.
 */
public class Controller implements IControllerForM, IControllerForV {

	/**
	 * Moottori, joka suorittaa simulaation.
	 */
	private IEngine engine;
	/**
	 * Käyttöliittymä, joka näyttää simulaation.
	 */
	private ISimulatorUI ui;
	/**
	 * Taso, joka valitaan simulaation alussa.
	 * Tämän päälle tallennetaan toinen taso, jos vaihdat tasoa sivupalkista.
	 */
	private String levelKey = "DEBUG world";

	/**
	 * Level_variableDao, joka tallentaa tason muuttujat tietokantaan.
	 */
	private Level_variableDao levelvariableDao = new Level_variableDao();

	/**
	 * ResultsDao, joka tallentaa simulaation tulokset tietokantaan.
	 */
	private ResultsDao resultsDao = new ResultsDao();

	/**
	 * @param ui Käyttöliittymä, joka näyttää simulaation.
	 */
	public Controller(ISimulatorUI ui) {
		this.ui = ui;
		this.engine = new CustomEngine(this, levelKey);
	}

	/**
	 * Aloita simulaatio.
	 * Simulaation aloitus nollaa käyttöliittymä canvaksen.
	 * Nullaa asiakas laskurin.
	 * Tekee uuden moottorin, johon laitetaan valittu taso.
	 * Asettaa simulaatioon päättymis ajan.
	 * Asettaa simulaation halutun viiveen.
	 * Aloittaa simulaation toisella säikeellä.
	 */
	@Override
	public void startSimulator() {
		ui.getVisualization().reset();
		Customer.resetCustomerCount();
		engine = new CustomEngine(this, levelKey); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(ui.getTime());
		engine.setDelay(ui.getDelay());
		((Thread) engine).start();
	}

	/**
	 * Tallenna simulaation tulokset SQL tietokantaan.
	 * Palvelupisteistä erotellaa tarkempia tietoja, riippuen palvelupiste tyypistä.
	 * @param level Taso, joka valitaan simulaation alussa.
	 */
	public void uploadResults(Level level) {
		Results results = new Results(getEngine().getSimulationTime(), level.getLevelName());
		resultsDao.persist(results);
		for (ServicePoint servicePoint : level.getServicePoints()) {
			if (servicePoint.getClass() == TrafficLights.class || servicePoint.getClass() == Crosswalk.class){
				Level_variables levelVariables = new Level_variables(results, servicePoint.getScheduledEventType(), servicePoint.getMean(), servicePoint.getMean2(), servicePoint.getVariance(), servicePoint.getVariance2(), servicePoint.getCarCount(), servicePoint.getMaxQueueSize());
				levelvariableDao.persist(levelVariables);
			} else if (servicePoint.getClass() == Goal.class) {
				Goal goal = (Goal) servicePoint;
				Level_variables levelVariables = new Level_variables(results, goal.getScheduledEventType(), goal.getCarCount(), goal.getAverageCompletionTime());
				levelvariableDao.persist(levelVariables);
			} else if (servicePoint.getMaxQueueSize() > 5){
				Level_variables levelVariables = new Level_variables(results, servicePoint.getScheduledEventType(), servicePoint.getMean(), servicePoint.getMean2(), servicePoint.getVariance(), servicePoint.getVariance2(), servicePoint.getCarCount(), servicePoint.getMaxQueueSize());
				levelvariableDao.persist(levelVariables);
			}
		}
	}

	/**
	 * Kasvata simulaation viivettä
	 */
	@Override
	public void slowdown() { // hidastetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*1.5));
	}

	/**
	 * Pienennä simulaation viivettä
	 */
	@Override
	public void speedup() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
	}

	/**
	 * Aseta käyttöliittymään simulaation nyky aika.
	 * @param time Aika, joka asetetaan käyttöliittymään simulaation nyky hetkisestä ajasta
	 */
	@Override
	public void showEndtime(double time) {
		Platform.runLater(() -> ui.setEndTime(time));
	}

	/**
	 * Piirrä palvelupiste canvakselle.
	 * Tämä metodi asettaa palvelupisteelle x, y ja rotation tiedot.
	 * @param level Taso, joka valitaan simulaation alussa.
	 * @param type Palvelupisteen tyyppi.
	 * @param x Palvelupisteen x-koordinaatti.
	 * @param y Palvelupisteen y-koordinaatti.
	 * @param rotation Palvelupisteen kääntö.
	 */
	@Override
	public void render(Level level, String type, double x, double y, String rotation) {
		ServicePoint servicePoint = level.getServicePoint(type);
		servicePoint.render(x, y, rotation);
		ui.getVisualization().addToRenderQueue(servicePoint);
	}

	/**
	 * setToCurrentLevel asettaa visuaalisointiin valitan tason.
	 * Tätä tasoa käytetään canvas piirroksen päivittämiseen.
	 * @param level Taso, joka valitaan simulaation alussa.
	 */
	public void setToCurrentLevel(Level level) {
		ui.getVisualization().setLevel(level);
	}

	/**
	 * Lisää asiakas visuaalisointi jonoon.
	 * Visuaalisointi pitää omaa kirjaa käyttäjistä, jotta ne voitaisiin nopeammin piirtää ja poistaa, joten ne pitää lisätä visuaalisointiin erikseen.
	 * @param customer Asiakas, joka lisätään jonoon.
	 */
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

	@Override
	public List<Level_variables> getLevelVariables(){
		return levelvariableDao.getAll();
	}
}
