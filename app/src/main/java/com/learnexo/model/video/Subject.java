package com.learnexo.model.video;

import com.learnexo.model.video.chapter.Chapter;

import java.util.Date;
import java.util.List;

public class Subject {
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private Department mDepartment;
    private Branch mBranch;

    public Department getDepartment() {
        return mDepartment;
    }

    public void setDepartment(Department department) {
        this.mDepartment = department;
    }

    public Branch getBranch() {
        return mBranch;
    }

    public void setBranch(Branch branch) {
        this.mBranch = branch;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private String subjectName;
    private List<Chapter> chapterList;
    private Date lastUpdated;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }
}
