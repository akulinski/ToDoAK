package com.akulinski.todoak.parsers;

import java.util.HashMap;
import java.util.Objects;

public class NoteDAO implements IResource{

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

    public NoteDAO(int userId, String title, boolean completed) {
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

    @Override
    public HashMap<String, String> generateObjectInfo() {
        HashMap<String,String> stringHashMap = new HashMap<>();
        stringHashMap.put("fk_user_id", String.valueOf(userId));
        stringHashMap.put("title",title);
        stringHashMap.put("completed", String.valueOf(completed));
        return stringHashMap;
    }

    @Override
    public IResource fromMap(HashMap<String, String> map) {
        this.userId = Integer.parseInt(Objects.requireNonNull(map.get("fk_user_id")));
        this.completed = Boolean.parseBoolean(map.get("completed"));
        this.title = map.get("title");
        this.id = Integer.parseInt(Objects.requireNonNull(map.get("id_note")));
        return this;
    }
}
