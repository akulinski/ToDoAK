package com.akulinski.todoak.utils;

public enum DbInfo {
    TABLE_NAME("note"),
    COLUMN_ID("id_note"),
    COLUMN_USER_ID("fk_user_id"),
    COLUMN_TITLE("title"),
    COLUMN_COMPLETED("completed");


    private String value;

    DbInfo(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
