package simu.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "level_variables")
public class Level_variables {
    @Id
    private int levelId;
    @Column(name = "servicePointName")
    private String servicePointName;
    @Column(name = "eventInterval")
    private double eventInterval;
    @Column(name = "leadTime")
    private double leadTime;

    public Level_variables(int levelId, String servicePointName, double eventInterval, double leadTime) {
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.eventInterval = eventInterval;
        this.leadTime = leadTime;
    }

    public Level_variables() {
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
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
}
