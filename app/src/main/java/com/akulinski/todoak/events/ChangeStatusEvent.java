package com.akulinski.todoak.events;

public class ChangeStatusEvent {

    private String title;
    private String newStatus;

    public ChangeStatusEvent(String title, String newStatus) {
        this.title = title;
        this.newStatus = newStatus;
    }

    public ChangeStatusEvent() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
