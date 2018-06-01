package com.learnexo.model.video;

import com.learnexo.model.video.chapter.Chapter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Subject {
    private String name;
    private Double mPrice;
    private Date lastUpdated;
    private Map<String,Chapter> mStringChapterMap;
    //placed for interests recycler view issue
    private boolean isChecked;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Chapter> getStringChapterMap() {
        return mStringChapterMap;
    }

    public void setStringChapterMap(Map<String, Chapter> stringChapterMap) {
        mStringChapterMap = stringChapterMap;
    }
}
