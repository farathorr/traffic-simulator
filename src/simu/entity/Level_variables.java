package simu.entity;


import jakarta.persistence.*;

@Entity
public class Level_variables {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Results levelId;
    private String servicePointName;
    private double mean1;
    private double mean2;
    private double variance1;
    private double variance2;
    private int carCount;
    private double averageTime;

    private int maxQueueSize;
    @Transient
    private LevelVariablesModel levelVariablesModel;

    public Level_variables(Results levelId, String servicePointName, double mean1, double mean2, double variance1, double variance2, int carCount, int maxQueueSize) {
        this.carCount = carCount;
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.mean1 = mean1;
        this.mean2 = mean2;
        this.variance1 = variance1;
        this.variance2 = variance2;
        this.maxQueueSize = maxQueueSize;
        this.levelVariablesModel = new LevelVariablesModel(this);
    }


    public Level_variables(Results levelId, String servicePointName, int carCount, double averageTime) {
        this.averageTime = averageTime;
        this.carCount = carCount;
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.levelVariablesModel = new LevelVariablesModel(this);
    }

    public Level_variables() {
    }

    public Results getLevelId() {
        return levelId;
    }

    public void setLevelId(Results levelId) {
        this.levelId = levelId;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }

    public double getMean1() {
        return mean1;
    }

    public void setMean1(double mean) {
        this.mean1 = mean;
    }

    public double getVariance1() {
        return variance1;
    }

    public void setVariance1(double variance) {
        this.variance1 = variance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LevelVariablesModel getLevelVariablesModel() {
        return levelVariablesModel;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public double getMean2() {
        return mean2;
    }

    public void setMean2(double mean2) {
        this.mean2 = mean2;
    }

    public double getVariance2() {
        return variance2;
    }

    public void setVariance2(double variance2) {
        this.variance2 = variance2;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }
}
