package com.akulinski.todoak.events;

public class SaveNoteEvent {

    private String title;
    private int noteId;
    private boolean completed;

    public SaveNoteEvent(String title, int noteId) {
        this.title = title;
        this.noteId = noteId;
    }

    public SaveNoteEvent(String title, int noteId, boolean completed) {
        this.title = title;
        this.noteId = noteId;
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean status) {
        this.completed = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
