package com.learnexo.model.video;

import com.learnexo.model.video.chapter.Chapter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject implements Serializable{
    private String mSubjectName;
    private Map<String,Chapter> mChapterMap=new HashMap<>();
    private Double mPrice;
    private Date lastUpdated;

    //placed for interests recycler view issue
    private transient boolean isChecked;

    public Subject() {}

    public Subject(boolean isChecked, Department department, Branch branch, Double price, String subjectName, List<Chapter> mChapterMap, Date lastUpdated) {
        this.isChecked = isChecked;
        mPrice = price;

        this.lastUpdated = lastUpdated;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSubjectName() {
        return mSubjectName;
    }

    public void setSubjectName(String subjectName) {
        mSubjectName = subjectName;
    }

    public Map<String, Chapter> getChapterMap() {
        return mChapterMap;
    }

    public void setChapterMap(Map<String, Chapter> chapterMap) {
        mChapterMap = chapterMap;
    }

}
