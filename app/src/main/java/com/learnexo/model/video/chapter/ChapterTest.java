package com.learnexo.model.video.chapter;

import java.util.Map;

class ChapterTest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ChapterTestQuestion> getStringChapterTestQuestionMap() {
        return mStringChapterTestQuestionMap;
    }

    public void setStringChapterTestQuestionMap(Map<String, ChapterTestQuestion> stringChapterTestQuestionMap) {
        mStringChapterTestQuestionMap = stringChapterTestQuestionMap;
    }

    private Map<String,ChapterTestQuestion> mStringChapterTestQuestionMap;

}
