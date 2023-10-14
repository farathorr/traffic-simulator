package simu.framework;

import simu.model.LevelController;

/**
 * Rajapinta, joka sisältää metodeja, joita kontrolleri käyttää.
 */
public interface IEngine {
	/**
	 * @param time Aika, joka asetetaan simulaation pituudeksi.
	 */
	public void setSimulationTime(double time);

	/**
	 * @param time Viive, jota simulaatio käyttää visualisoinnin hidastamiseen tai nopeuttamiseen.
	 */
	public void setDelay(long time);

	/**
	 * @return Viiveen getteri.
	 */
	public long getDelay();

	/**
	 * @return Kellon getteri.
	 */
	public Clock getClock();

	/**
	 * @return LevelControllerin getteri.
	 */
	public LevelController getLevelController();
}
