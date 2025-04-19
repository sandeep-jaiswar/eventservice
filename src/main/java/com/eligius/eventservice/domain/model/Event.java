package com.eligius.eventservice.domain.model;

public class Event {
    private String eventType;
    private String eventKey;
    private String eventValue;

    public Event(String eventKey, String eventType, String eventValue) {
        this.eventKey = eventKey;
        this.eventType = eventType;
        this.eventValue = eventValue;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getEventKey() {
        return eventKey;
    }
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
    public String getEventValue() {
        return eventValue;
    }
    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }
}
