package com.example.skillswap;

public class User {
    private String name;
    private String image;
    private String userId;
    private String occupation;
    private String searchName;

    public User() {
        // Required empty constructor for Firestore
    }

    // Constructor
    public User(String name, String image, String userId, String occupation, String searchName) {
        this.name = name;
        this.image = image;
        this.userId = userId;
        this.occupation = occupation;
        this.searchName = searchName;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for image
    public String getImage() {
        return image;
    }

    // Setter for image
    public void setImage(String image) {
        this.image = image;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for occupation
    public String getOccupation() {
        return occupation;
    }

    // Setter for occupation
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    // Getter for searchName
    public String getSearchName() {
        return searchName;
    }

    // Setter for searchName
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}