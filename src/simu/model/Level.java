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

    public Level add(ServicePoint point) {
        servicePoints.put(point.getScheduledEventType(), point);
        return this;
    }

    public void next(String next) {
        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.add(next);
        this.nextPoints.put(this, nextPoints);
    }

    public void next(String[] next) {
        ArrayList<String> nextPoints = new ArrayList<>();
        nextPoints.addAll(Arrays.asList(next));
        this.nextPoints.put(this, nextPoints);
    }

    public void arrival(ArrivalProcess arrivalProcess) {
        arrivalProcesses.put(arrivalProcess.getScheduledEventType(), arrivalProcess);
    }
}
