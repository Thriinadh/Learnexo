package com.learnexo.model.video.chapter;

import java.io.Serializable;
import java.util.Map;

class ChapterTest implements Serializable{
    private String chapTestName;
    private Map<String,ChapterTestQuestion> chapterTestQuestionMap;


    public String getChapTestName() {
        return chapTestName;
    }

    public void setChapTestName(String chapTestName) {
        this.chapTestName = chapTestName;
    }

    public Map<String, ChapterTestQuestion> getChapterTestQuestionMap() {
        return chapterTestQuestionMap;
    }

    public void setChapterTestQuestionMap(Map<String, ChapterTestQuestion> chapterTestQuestionMap) {
        this.chapterTestQuestionMap = chapterTestQuestionMap;
    }

}
