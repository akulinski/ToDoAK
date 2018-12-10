package com.akulinski.todoak.utils;

public enum HolderStatus {

    SHOWING_TEXT("SHOWING"),
    EDITING("EDITING");

    private String value;

    HolderStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
