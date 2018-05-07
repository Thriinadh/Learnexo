package com.learnexo.model.user;

import com.learnexo.model.core.Gender;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    public String email;
    public String dpUrl;
    private Gender gender;

    public User() {}

    public User(String firstName, String lastName, Gender gender, String dpUrl, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dpUrl = dpUrl;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
