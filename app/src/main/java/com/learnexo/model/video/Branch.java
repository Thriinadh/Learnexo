package com.learnexo.model.video;

import java.util.Map;

public class Branch {

    private String branchName;
    private Map<String, Subject> subjectMap;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Map<String, Subject> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<String, Subject> subjectMap) {
        this.subjectMap = subjectMap;
    }

}
