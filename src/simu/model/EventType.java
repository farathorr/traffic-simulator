package simu.model;

import simu.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
	ARR1, ARR2, DEP1, DEP2, DEP3, TRAFFIC_LIGHTS, LIGHT_SWITCH, TURN_LEFT, TURN_RIGHT

}
