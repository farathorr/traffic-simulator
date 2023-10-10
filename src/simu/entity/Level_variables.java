package simu.entity;


import jakarta.persistence.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class Level_variables {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Results levelId;
    private String servicePointName;
    private double eventInterval;
    private double leadTime;

    @Transient
    private LevelVariablesModel levelVariablesModel;

    public Level_variables(Results levelId, String servicePointName, double eventInterval, double leadTime) {
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.eventInterval = eventInterval;
        this.leadTime = leadTime;
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

    public double getEventInterval() {
        return eventInterval;
    }

    public void setEventInterval(double eventInterval) {
        this.eventInterval = eventInterval;
    }

    public double getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(double leadTime) {
        this.leadTime = leadTime;
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
}
