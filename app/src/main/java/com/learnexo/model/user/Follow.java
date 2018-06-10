package com.learnexo.model.user;

public class Follow {
    private String firstName;
    public String dpUrl;
    private String description;

    public Follow() {}

    public Follow(String firstName, String dpUrl, String description) {
        this.firstName = firstName;
        this.dpUrl = dpUrl;
        this.description = description;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
