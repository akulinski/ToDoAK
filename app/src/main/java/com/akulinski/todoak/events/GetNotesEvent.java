package com.akulinski.todoak.events;

import com.google.gson.JsonArray;

public final class GetNotesEvent {

    private boolean isSuccesful;

    private JsonArray jsonObject;

    public GetNotesEvent(boolean isSuccesful, JsonArray jsonObject) {
        this.isSuccesful = isSuccesful;
        this.jsonObject = jsonObject;
    }

    public GetNotesEvent(boolean isSuccesful) {
        this.isSuccesful = isSuccesful;
    }

    public boolean isSuccesful() {
        return isSuccesful;
    }

    public void setSuccesful(boolean succesful) {
        isSuccesful = succesful;
    }

    public JsonArray getJsonArray() {
        return jsonObject;
    }

    public void setJsonArray(JsonArray jsonObject) {
        this.jsonObject = jsonObject;
    }
}
