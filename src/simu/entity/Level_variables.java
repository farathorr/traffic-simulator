package simu.entity;


import jakarta.persistence.*;
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
    private SimpleStringProperty servicePointNameProperty;
    private SimpleDoubleProperty eventIntervalProperty;
    private SimpleDoubleProperty leadTimeProperty;
    private

    public Level_variables(Results levelId, String servicePointName, double eventInterval, double leadTime) {
        this.levelId = levelId;
        this.servicePointName = servicePointName;
        this.eventInterval = eventInterval;
        this.leadTime = leadTime;
        this.servicePointNameProperty = new SimpleStringProperty(servicePointName);
        this.eventIntervalProperty = new SimpleDoubleProperty(eventInterval);
        this.leadTimeProperty = new SimpleDoubleProperty(leadTime);
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

    public String getServicePointNameProperty() {
        return servicePointNameProperty.get();
    }

    public SimpleStringProperty servicePointNamePropertyProperty() {
        return servicePointNameProperty;
    }

    public void setServicePointNameProperty(String servicePointNameProperty) {
        this.servicePointNameProperty.set(servicePointNameProperty);
    }

    public double getEventIntervalProperty() {
        return eventIntervalProperty.get();
    }

    public SimpleDoubleProperty eventIntervalPropertyProperty() {
        return eventIntervalProperty;
    }

    public void setEventIntervalProperty(double eventIntervalProperty) {
        this.eventIntervalProperty.set(eventIntervalProperty);
    }

    public double getLeadTimeProperty() {
        return leadTimeProperty.get();
    }

    public SimpleDoubleProperty leadTimePropertyProperty() {
        return leadTimeProperty;
    }

    public void setLeadTimeProperty(double leadTimeProperty) {
        this.leadTimeProperty.set(leadTimeProperty);
    }
}
