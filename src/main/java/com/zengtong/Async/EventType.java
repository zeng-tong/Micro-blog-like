package com.zengtong.Async;

public enum EventType {

    LOGIN(0),
    LIKE(1),
    COMMENT(2),
    REGISTER(3);

    private int value;


    EventType(int i) {
        this.value = i;
    }


    public int getValue() {
        return value;
    }


}
