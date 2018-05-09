package com.learnexo.model.video;

import java.util.List;

public class Branch {

    private String name;
    private List<Subject> mSubjects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjects() {
        return mSubjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.mSubjects = subjects;
    }
}
