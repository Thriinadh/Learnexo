package com.learnexo.model.video;

import com.learnexo.model.video.chapter.Chapter;

import java.util.Date;
import java.util.List;

public class Topic {
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private Branch branch;
    private SubBranch subBranch;

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public SubBranch getSubBranch() {
        return subBranch;
    }

    public void setSubBranch(SubBranch subBranch) {
        this.subBranch = subBranch;
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
