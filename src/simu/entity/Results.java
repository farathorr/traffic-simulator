package simu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "results")
public class Results {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name = "carCount")
    private int carCount;
    @Column(name = "averageTime")
    private double averageTime;
    @Column(name = "simulationTime")
    private double simulationTime;
    @Column(name = "simulationLevel")
    private String simulationLevel;

    public Results(int carCount, double averageTime, double simulationTime, String simulationLevel) {
        this.carCount = carCount;
        this.averageTime = averageTime;
        this.simulationTime = simulationTime;
        this.simulationLevel = simulationLevel;
    }

    public Results() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }

    public String getSimulationLevel() {
        return simulationLevel;
    }

    public void setSimulationLevel(String simulationLevel) {
        this.simulationLevel = simulationLevel;
    }

}
