package simu.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LevelVariablesModel {
    private final StringProperty servicePointName = new SimpleStringProperty();
    private final DoubleProperty eventInterval = new SimpleDoubleProperty();
    private final DoubleProperty leadTime = new SimpleDoubleProperty();

    public LevelVariablesModel(Level_variables levelVariables) {
        this.servicePointName.set(levelVariables.getServicePointName());
        this.eventInterval.set(levelVariables.getEventInterval());
        this.leadTime.set(levelVariables.getLeadTime());
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
}
