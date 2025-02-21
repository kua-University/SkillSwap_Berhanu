package com.example.skillswap;

public class NotificationModel {
    private String notificationText;
    private String postId;

    public NotificationModel(String notificationText, String postId) {
        this.notificationText = notificationText;
        this.postId = postId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}