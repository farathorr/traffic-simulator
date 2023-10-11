package simu.entity;

import jakarta.persistence.*;

@Entity
public class Results {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private double simulationTime;
    private String simulationLevel;

    public Results(double simulationTime, String simulationLevel) {
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
