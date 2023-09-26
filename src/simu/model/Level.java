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

    public void add(ServicePoint point) {
        servicePoints.put(point.getScheduledEventType(), point);
    }

    public void add(ServicePoint point, String nextPoint) {
        servicePoints.put(point.getScheduledEventType(), point);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.add(nextPoint);
        this.nextPoints.put(point, nextPoints);
    }

    public void add(ServicePoint point, String[] nextPoint) {
        servicePoints.put(point.getScheduledEventType(), point);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(nextPoint));
        this.nextPoints.put(point, nextPoints);
    }

    public void arrival(ArrivalProcess arrivalProcess, String nextPoint) {
        arrivalProcesses.put(arrivalProcess.getScheduledEventType(), arrivalProcess);

        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.add(nextPoint);
        this.nextPoints.put(arrivalProcess, nextPoints);
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
        return nextPoints.containsKey(servicePoints.get(key));
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

    public ArrivalProcess getArrivalProcess(String key) {
        return arrivalProcesses.get(key);
    }

    public ArrayList<ServicePoint> getServicePoints() {
        return new ArrayList<>(servicePoints.values());
    }
}
