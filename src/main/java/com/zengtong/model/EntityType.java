package com.zengtong.model;

public enum EntityType {

    WEIBO(0),
    COMMENT(1),
    USER(2);

    public int getValue() {
        return value;
    }

    EntityType(int value){
        this.value = value;
    }

    private int value;



}
