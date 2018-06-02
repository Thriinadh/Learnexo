package com.learnexo.model.video.chapter;

import com.learnexo.model.video.VideoLesson;

import java.io.Serializable;
import java.util.Map;

public class Chapter implements Serializable {
    private String mChapterName;
    private Map<String,VideoLesson> mVideoLessonMap;
    private Map<String,ChapterTest> mChapterTestMap;


    public String getChapterName() {
        return mChapterName;
    }

    public void setChapterName(String chapterName) {
        mChapterName = chapterName;
    }

    public Map<String, VideoLesson> getVideoLessonMap() {
        return mVideoLessonMap;
    }

    public void setVideoLessonMap(Map<String, VideoLesson> videoLessonMap) {
        mVideoLessonMap = videoLessonMap;
    }

    public Map<String, ChapterTest> getChapterTestMap() {
        return mChapterTestMap;
    }

    public void setChapterTestMap(Map<String, ChapterTest> chapterTestMap) {
        mChapterTestMap = chapterTestMap;
    }

}
