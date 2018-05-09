package com.learnexo.model.video;

import java.util.Map;

public class Branch {

    private String name;
    private Map<Subject, Boolean> mSubjects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
