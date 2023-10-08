package simu.model;

import java.util.HashMap;

public class LevelSettings {
    private HashMap<String, Double> settings = new HashMap<>();
    private static LevelSettings instance = null;

    private LevelSettings() {

    }

    public static LevelSettings getInstance() {
        if (instance == null) instance = new LevelSettings();

        return instance;
    }

    public void add(String key, Double value) {
        settings.put(key, value);
    }

    public boolean has(String key) {
        return settings.containsKey(key);
    }

    public double get(String key) {
        return settings.get(key);
    }
}
