package simu.model;

import java.util.HashMap;

/**
 * LevelSettings-luokka, joka sisältää tason asetukset.
 * Asetuksiin kuuluu liikenne valojen ja suojateiden ylitys ajat ja kaikki muut suureet, jotka ovat muokattavissa käyttöliittymön sivupalkista.
 */
public class LevelSettings {
    /**
     * HashMap, johon tallennetaan kaikki asetukset.
     * Avain koostuu tason nimestä, palvelupisteen tyypistä ja asetuksesta mitä muutat.
     */
    private HashMap<String, Double> settings = new HashMap<>();
    /**
     * LevelSettings instanssi.
     */
    private static LevelSettings instance = null;

    private LevelSettings() {

    }

    /**
     * Palauttaa LevelSettings instanssin.
     * @return LevelSettings instanssi.
     */
    public static LevelSettings getInstance() {
        if (instance == null) instance = new LevelSettings();

        return instance;
    }

    /**
     * Tallenna asetuksen arvo.
     * @param key Avain johon arvo tallennetaan.
     * @param value Arvo joka tallennetaan.
     */
    public void add(String key, Double value) {
        settings.put(key, value);
    }

    /**
     * Etsi onko asetusta tallennettu kyseisellä avaimella.
     * @param key Avain jota etsitään.
     * @return Palauttaa true jos avain löytyy, muuten false.
     */
    public boolean has(String key) {
        return settings.containsKey(key);
    }

    /**
     * Hae asetuksen arvo.
     * @param key Avain jota etsitään.
     * @return Palauttaa asetuksen arvon.
     */
    public double get(String key) {
        return settings.get(key);
    }
}
