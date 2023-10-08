package simu.model;

import simu.framework.ArrivalProcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Level {
    private Map<String, ServicePoint> servicePoints = new HashMap<>();
    private Map<String, ArrivalProcess> arrivalProcesses = new HashMap<>();
    private Map<Object, ArrayList<String>> nextPoints = new HashMap<>();
    private String levelName;
    private LevelSettings settings = LevelSettings.getInstance();

    public Level(String levelName) {
        this.levelName = levelName;
    }

    public void add(ServicePoint point) {
        servicePoints.put(point.getScheduledEventType(), point);
        point.setLevel(this);
        point.init();
    }

    public void add(ServicePoint point, String nextPoint) {
        add(point, new String[]{nextPoint});
    }

    public void add(ServicePoint point, String[] nextPoint) {
        servicePoints.put(point.getScheduledEventType(), point);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(nextPoint));
        this.nextPoints.put(point, nextPoints);
        point.setLevel(this);
        point.init();
    }

    public void arrival(ArrivalProcess arrivalProcess, String nextPoint) {
        arrival(arrivalProcess, new String[]{nextPoint});
    }

    public void arrival(ArrivalProcess arrivalProcess, String[] nextPoint) {
        arrivalProcesses.put(arrivalProcess.getScheduledEventType(), arrivalProcess);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(nextPoint));
        this.nextPoints.put(arrivalProcess, nextPoints);
    }

    public void startSimulation() {
        for (ArrivalProcess arrivalProcess : arrivalProcesses.values()) {
            arrivalProcess.generateNext();
        }
    }

    public boolean hasNextServicePoint(String key) {
        ServicePoint servicePoint = servicePoints.get(key);
        return nextPoints.containsKey(servicePoint);
    }

    public boolean hasNextServicePoint(ServicePoint servicePoint) {
        return nextPoints.containsKey(servicePoint);
    }

    public ServicePoint getServicePoint(String key) {
        return servicePoints.get(key);
    }

    public boolean isArrivalProcess(String type) {
        return arrivalProcesses.containsKey(type);
    }
    public ServicePoint getNextServicePoint(Object servicePoint) {
        int r = (int) Math.floor(Math.random() * nextPoints.get(servicePoint).size());
        return servicePoints.get(nextPoints.get(servicePoint).get(r));
    }

    public ServicePoint getNextRoundaboutServicePoint(Object servicePoint, boolean isExiting) {
        if (isExiting) {
            int r = (int) Math.ceil(Math.random() * nextPoints.get(servicePoint).size() - 2) + 1;
            return servicePoints.get(nextPoints.get(servicePoint).get(r));
        }
        return servicePoints.get(nextPoints.get(servicePoint).get(0));
    }

    public boolean roundaboutHasExitPoint(Object servicePoint) {
        return nextPoints.get(servicePoint).size() > 1;
    }

    public int getNextServicePointCount(String key) {
        ServicePoint servicePoint = servicePoints.get(key);
        if (nextPoints.containsKey(servicePoint)) return nextPoints.get(servicePoint).size();
        return 0;
    }

    public ArrivalProcess getArrivalProcess(String key) {
        return arrivalProcesses.get(key);
    }

    public ArrayList<ServicePoint> getServicePoints() {
        return new ArrayList<>(servicePoints.values());
    }

    public boolean hasGenerator1(String key) {
        return settings.has(levelName + key + "generator1");
    }

    public boolean hasGenerator2(String key) {
        return settings.has(levelName + key + "generator2");
    }

    public double getGenerator1(String key) {
        return settings.get(levelName + key + "generator1");
    }

    public double getGenerator2(String key) {
        return settings.get(levelName + key + "generator2");
    }

    public String getLevelName() {
        return levelName;
    }
}
