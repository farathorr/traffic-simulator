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
    private double mean;
    private double variance;
    private int carCount;
    private double averageTime;
    @Transient
    private LevelVariablesModel levelVariablesModel;

    public Level_variables(Results levelId, String servicePointName, double mean, double variance, int carCount) {
        this.carCount = carCount;
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.mean = mean;
        this.variance = variance;
        this.levelVariablesModel = new LevelVariablesModel(this);
    }

    public Level_variables(Results levelId, String servicePointName, double mean, double variance) {
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.mean = mean;
        this.variance = variance;
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

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
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
}
