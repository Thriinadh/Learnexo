package com.learnexo.model.video;

import java.util.Map;

public class Branch {

    private String name;
    private Map<String, Subject> mStringSubjectMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Subject> getStringSubjectMap() {
        return mStringSubjectMap;
    }

    public void setStringSubjectMap(Map<String, Subject> stringSubjectMap) {
        mStringSubjectMap = stringSubjectMap;
    }

}
