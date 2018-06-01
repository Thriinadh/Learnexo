package com.learnexo.model.video.chapter;

import java.util.Map;

class ChapterTest {
    private String chapTestName;

    public String getChapTestName() {
        return chapTestName;
    }

    public void setChapTestName(String chapTestName) {
        this.chapTestName = chapTestName;
    }

    public Map<String, ChapterTestQuestion> getStringChapterTestQuestionMap() {
        return mStringChapterTestQuestionMap;
    }

    public void setStringChapterTestQuestionMap(Map<String, ChapterTestQuestion> stringChapterTestQuestionMap) {
        mStringChapterTestQuestionMap = stringChapterTestQuestionMap;
    }

    private Map<String,ChapterTestQuestion> mStringChapterTestQuestionMap;

}
