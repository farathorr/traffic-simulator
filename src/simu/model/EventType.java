package simu.model;

import simu.framework.IEventType;

// TODO:
// Tapahtumien tyypit määritellään simulointimallin vaatimusten perusteella
public enum EventType implements IEventType {
    ARR1, TRAFFIC_LIGHTS, LIGHT_SWITCH, TURN_LEFT, TURN_RIGHT, INTERSECTION, CROSSWALK, ROAD_CROSSING, ROUNDABOUT_TOP ,ROUNDABOUT_BOTTOM ,ROUNDABOUT_RIGHT ,ROUNDABOUT_LEFT
}
