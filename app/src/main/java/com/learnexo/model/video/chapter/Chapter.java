package com.learnexo.model.video.chapter;

import com.learnexo.model.video.VideoLesson;

import java.util.Map;

public class Chapter {
    private String mChapterName;
    private Map<String,VideoLesson> mStringVideoLessonMap;

    public String getChapterName() {
        return mChapterName;
    }

    public void setChapterName(String chapterName) {
        mChapterName = chapterName;
    }

    public Map<String, VideoLesson> getStringVideoLessonMap() {
        return mStringVideoLessonMap;
    }

    public void setStringVideoLessonMap(Map<String, VideoLesson> stringVideoLessonMap) {
        mStringVideoLessonMap = stringVideoLessonMap;
    }

    public Map<String, ChapterTest> getStringChapterTestMap() {
        return mStringChapterTestMap;
    }

    public void setStringChapterTestMap(Map<String, ChapterTest> stringChapterTestMap) {
        mStringChapterTestMap = stringChapterTestMap;
    }

    private Map<String,ChapterTest> mStringChapterTestMap;

}
