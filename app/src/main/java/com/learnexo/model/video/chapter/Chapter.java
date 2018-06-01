package com.learnexo.model.video.chapter;

import com.learnexo.model.video.VideoLesson;

import java.util.Map;

public class Chapter {
    private String name;
    private Map<String,VideoLesson> mStringVideoLessonMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
