package com.learnexo.tabfragments;

import java.util.List;

class Subject {

    private String name;
    private List<Chapter> chapterList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }
}
