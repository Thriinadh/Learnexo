package com.learnexo.model.video.chapter;

import com.learnexo.model.video.VideoLesson;

import java.io.Serializable;
import java.util.Map;

public class Chapter implements Serializable {
    private String chapterName;
    private Map<String,VideoLesson> videoLessonMap;
    private Map<String,ChapterTest> chapterTestMap;


    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Map<String, VideoLesson> getVideoLessonMap() {
        return videoLessonMap;
    }

    public void setVideoLessonMap(Map<String, VideoLesson> videoLessonMap) {
        this.videoLessonMap = videoLessonMap;
    }

    public Map<String, ChapterTest> getChapterTestMap() {
        return chapterTestMap;
    }

    public void setChapterTestMap(Map<String, ChapterTest> chapterTestMap) {
        this.chapterTestMap = chapterTestMap;
    }

}
