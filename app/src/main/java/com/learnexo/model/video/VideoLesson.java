package com.learnexo.model.video;

import java.io.Serializable;

public class VideoLesson implements Serializable{
    private String videoName;
    private String uri;
    private boolean isFreeVideo;
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoName() {
        return videoName;
    }

    public boolean isFreeVideo() {
        return isFreeVideo;
    }

    public void setFreeVideo(boolean freeVideo) {
        isFreeVideo = freeVideo;
    }

    public void setVideoName(String videoName) {

        this.videoName = videoName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
