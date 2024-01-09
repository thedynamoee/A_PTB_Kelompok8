package com.eleco.model;

public class NotificationItem {
    private String notification_id;
    private String title;
    private Long timeStamp;

    public  NotificationItem(){

    }



    public NotificationItem(String notification_id, String title, Long timeStamp) {
        this.notification_id = notification_id;
        this.title = title;
        this.timeStamp = timeStamp;
    }
    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }




}
