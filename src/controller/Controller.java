package controller;

import javafx.application.Platform;
import simu.framework.IEngine;
import simu.model.CustomEngine;
import view.ISimulatorUI;

public class Controller implements IControllerForM, IControllerForV {   // UUSI
	
	private IEngine moottori;
	private ISimulatorUI ui;
	
	public Controller(ISimulatorUI ui) {
		this.ui = ui;
		
	}

	
	// Moottorin ohjausta:
		
	@Override
	public void startSimulator() {
		moottori = new CustomEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		moottori.setSimulationTime(ui.getTime());
		moottori.setDelay(ui.getDelay());
		ui.getVisualization().clearScreen();
		((Thread)moottori).start();
		//((Thread)moottori).run(); // Ei missään tapauksessa näin. Miksi?		
	}
	
	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		moottori.setDelay((long)(moottori.getDelay()*1.10));
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		moottori.setDelay((long)(moottori.getDelay()*0.9));
	}
	
	
	
	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen:
		
	@Override
	public void showEndtime(double aika) {
		Platform.runLater(()->ui.setEndTime(aika));
	}

	
	@Override
	public void visualizeCustomer() {
		Platform.runLater(new Runnable(){
			public void run(){
				ui.getVisualization().newCustomer();
			}
		});
	}



}
