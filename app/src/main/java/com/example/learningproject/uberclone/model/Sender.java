package com.example.learningproject.uberclone.model;

public class Sender {
    public String to;
    public com.iramml.uberclone.riderapp.model.fcm.Notification notification;

    public Sender(String to, com.iramml.uberclone.riderapp.model.fcm.Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public Sender() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public com.iramml.uberclone.riderapp.model.fcm.Notification getNotification() {
        return notification;
    }

    public void setNotification(com.iramml.uberclone.riderapp.model.fcm.Notification notification) {
        this.notification = notification;
    }
}
