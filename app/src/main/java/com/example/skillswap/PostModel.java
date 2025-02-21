package com.example.skillswap;

import java.util.List;

public class PostModel {
    private String userId;
    private String content;
    private List<String> likes;
    private String userName; // Add userName field

    public PostModel() {
        // Required empty constructor for Firestore
    }

    // Constructor
    public PostModel(String userId, String content, List<String> likes, String userName) {
        this.userId = userId;
        this.content = content;
        this.likes = likes;
        this.userName = userName;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for content
    public String getContent() {
        return content;
    }

    // Setter for content
    public void setContent(String content) {
        this.content = content;
    }

    // Getter for likes
    public List<String> getLikes() {
        return likes;
    }

    // Setter for likes
    public void setLikes(List<String> likes) {
        this.likes = likes;
    }
    // Getter for userName
    public String getUserName() {
        return userName;
    }

    // Setter for userName
    public void setUserName(String userName) {
        this.userName = userName;
    }
}