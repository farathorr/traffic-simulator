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
        point.setLevel(this);
//        String className = point.getClass().getSimpleName();
//        switch(className) {
//            case "TrafficLights" -> {
//                arrivalProcesses.put(className, )
//            }
//        }
//        System.out.println(point.getClass().getSimpleName());
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
        ServicePoint servicePoint = servicePoints.get(key);
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
}
