package com.learnexo.model.video;

import java.util.Map;

public class Branch {

    private String mBranchName;
    private Map<String, Subject> mStringSubjectMap;

    public String getBranchName() {
        return mBranchName;
    }

    public void setBranchName(String branchName) {
        mBranchName = branchName;
    }

    public Map<String, Subject> getStringSubjectMap() {
        return mStringSubjectMap;
    }

    public void setStringSubjectMap(Map<String, Subject> stringSubjectMap) {
        mStringSubjectMap = stringSubjectMap;
    }

}
