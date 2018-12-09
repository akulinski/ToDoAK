package com.akulinski.todoak.events;

public class RemoveNoteEvent {
    private int id;

    public RemoveNoteEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
