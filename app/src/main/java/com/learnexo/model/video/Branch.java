package com.learnexo.model.video;

import java.util.Map;

public class Branch {

    private String mBranchName;
    private Map<String, Subject> mSubjectMap;

    public String getBranchName() {
        return mBranchName;
    }

    public void setBranchName(String branchName) {
        mBranchName = branchName;
    }

    public Map<String, Subject> getSubjectMap() {
        return mSubjectMap;
    }

    public void setSubjectMap(Map<String, Subject> subjectMap) {
        mSubjectMap = subjectMap;
    }

}
