package com.zengtong.Async;

import java.util.Map;

public class EventModel {

    private EventType eventType;

    private int user_id;

    private int to_id;

    private int from_id;

    private int entity_type;

    private int entity_id;

    private Map<String ,String> ext;

    public EventModel() {
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getUser_id() {
        return user_id;
    }

    public EventModel setUser_id(int user_id) {
        this.user_id = user_id;
        return this;
    }

    public int getTo_id() {
        return to_id;
    }

    public EventModel setTo_id(int to_id) {
        this.to_id = to_id;
        return this;
    }

    public int getFrom_id() {
        return from_id;
    }

    public EventModel setFrom_id(int from_id) {
        this.from_id = from_id;
        return this;
    }

    public int getEntity_type() {
        return entity_type;
    }

    public EventModel setEntity_type(int entity_type) {
        this.entity_type = entity_type;
        return this;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public EventModel setEntity_id(int entity_id) {
        this.entity_id = entity_id;
        return this;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public EventModel setExt(Map<String, String> ext) {
        this.ext = ext;
        return this;
    }
}
