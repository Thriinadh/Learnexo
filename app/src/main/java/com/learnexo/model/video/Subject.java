package com.learnexo.model.video;

import com.learnexo.model.video.chapter.Chapter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject implements Serializable{
    private String subjectName;
    private String overView;
    private Map<String,Chapter> chapterMap =new HashMap<>();
    private Double price;
    private Date lastUpdated;

    //placed for interests recycler view issue
    private transient boolean isChecked;

    public Subject() {}

    public Subject(boolean isChecked, Department department, Branch branch, Double price, String subjectName, List<Chapter> mChapterMap, Date lastUpdated) {
        this.isChecked = isChecked;
        this.price = price;

        this.lastUpdated = lastUpdated;
    }
    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Map<String, Chapter> getChapterMap() {
        return chapterMap;
    }

    public void setChapterMap(Map<String, Chapter> chapterMap) {
        this.chapterMap = chapterMap;
    }

}
