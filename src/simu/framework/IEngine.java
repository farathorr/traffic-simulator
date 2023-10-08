package simu.framework;

import simu.model.LevelController;

public interface IEngine { // UUSI
		
	// Kontrolleri käyttää tätä rajapintaa
	
	public void setSimulationTime(double time);
	public void setDelay(long time);
	public long getDelay();
	public Clock getClock();

	public LevelController getLevelController();
}
