package simu.entity;

import jakarta.persistence.*;

/**
 * Tämä on Results luokka joka sisältää Results olion ominaisuudet ja metodit.
 */
@Entity
public class Results {
    /**
     * @param id on Results olion id.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    /**
     * @param simulationTime on Results olion yksi ominaisuus joka ottaa simulaatioajan itselleen.
     */
    private double simulationTime;
    /**
     * @param simulationLevel on Results olion yksi ominaisuus joka ottaa simulaatiotason itselleen.
     */
    private String simulationLevel;

    /**
     * @param simulationTime on Results olion yksi ominaisuus joka ottaa simulaatioajan itselleen.
     * @param simulationLevel on Results olion yksi ominaisuus joka ottaa simulaatiotason itselleen.
     */
    public Results(double simulationTime, String simulationLevel) {
        this.simulationTime = simulationTime;
        this.simulationLevel = simulationLevel;
    }

    /**
     * Vaadittu konstruktori jotta Results luokka toimisi Entity luokkana.
     */
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
