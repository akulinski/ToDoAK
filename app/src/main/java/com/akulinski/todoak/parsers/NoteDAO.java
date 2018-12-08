package com.akulinski.todoak.parsers;

public class NoteDAO {

    private int id;
    private int userId;
    private String title;
    private boolean completed;

    public NoteDAO(int id, int userId, String title, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.completed = completed;
    }

    public NoteDAO(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
