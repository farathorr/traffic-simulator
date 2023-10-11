package simu.entity;

import javafx.beans.property.*;

public class LevelVariablesModel {
    private final StringProperty servicePointName = new SimpleStringProperty();
    private final DoubleProperty eventInterval = new SimpleDoubleProperty();
    private final DoubleProperty leadTime = new SimpleDoubleProperty();
    private final IntegerProperty  carCount = new SimpleIntegerProperty();
    private final DoubleProperty averageTime = new SimpleDoubleProperty();

    public LevelVariablesModel(Level_variables levelVariables) {
        this.servicePointName.set(levelVariables.getServicePointName());
        this.eventInterval.set(levelVariables.getMean());
        this.leadTime.set(levelVariables.getVariance());
        this.carCount.set(levelVariables.getCarCount());
        this.averageTime.set(levelVariables.getAverageTime());
    }



    public String getServicePointName() {
        return servicePointName.get();
    }

    public StringProperty servicePointNameProperty() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName.set(servicePointName);
    }

    public double getEventInterval() {
        return eventInterval.get();
    }

    public DoubleProperty eventIntervalProperty() {
        return eventInterval;
    }

    public void setEventInterval(double eventInterval) {
        this.eventInterval.set(eventInterval);
    }

    public double getLeadTime() {
        return leadTime.get();
    }

    public DoubleProperty leadTimeProperty() {
        return leadTime;
    }

    public void setLeadTime(double leadTime) {
        this.leadTime.set(leadTime);
    }

    public int getCarCount() {
        return carCount.get();
    }

    public IntegerProperty carCountProperty() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount.set(carCount);
    }

    public double getAverageTime() {
        return averageTime.get();
    }

    public DoubleProperty averageTimeProperty() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime.set(averageTime);
    }
}
