package simu.entity;

import javafx.beans.property.*;

public class LevelVariablesModel {
    private final StringProperty servicePointName = new SimpleStringProperty();
    private final DoubleProperty mean1 = new SimpleDoubleProperty();
    private final DoubleProperty mean2 = new SimpleDoubleProperty();
    private final DoubleProperty variance1 = new SimpleDoubleProperty();
    private final DoubleProperty variance2 = new SimpleDoubleProperty();
    private final IntegerProperty  carCount = new SimpleIntegerProperty();
    private final DoubleProperty averageTime = new SimpleDoubleProperty();
    private final IntegerProperty maxQueueSize = new SimpleIntegerProperty();

    public LevelVariablesModel(Level_variables levelVariables) {
        this.servicePointName.set(levelVariables.getServicePointName());
        this.mean1.set(levelVariables.getMean1());
        this.mean2.set(levelVariables.getMean2());
        this.variance1.set(levelVariables.getVariance1());
        this.variance2.set(levelVariables.getVariance2());
        this.carCount.set(levelVariables.getCarCount());
        this.averageTime.set(levelVariables.getAverageTime());
        this.maxQueueSize.set(levelVariables.getMaxQueueSize());
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

    public double getMean1() {
        return mean1.get();
    }

    public DoubleProperty mean1Property() {
        return mean1;
    }

    public void setMean1(double mean1) {
        this.mean1.set(mean1);
    }

    public double getVariance1() {
        return variance1.get();
    }

    public DoubleProperty variance1Property() {
        return variance1;
    }

    public void setVariance1(double variance1) {
        this.variance1.set(variance1);
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

    public double getMean2() {
        return mean2.get();
    }

    public DoubleProperty mean2Property() {
        return mean2;
    }

    public void setMean2(double mean2) {
        this.mean2.set(mean2);
    }

    public double getVariance2() {
        return variance2.get();
    }

    public DoubleProperty variance2Property() {
        return variance2;
    }

    public void setVariance2(double variance2) {
        this.variance2.set(variance2);
    }

    public int getMaxQueueSize() {
        return maxQueueSize.get();
    }

    public IntegerProperty maxQueueSizeProperty() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize.set(maxQueueSize);
    }
}
