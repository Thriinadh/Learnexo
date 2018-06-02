package com.learnexo.model.video;

import java.io.Serializable;

public class VideoLesson implements Serializable{
    private String videoName;
    private String uri;
    private boolean isFreeVideo;
    private String duration;

    private long views;
    private long upVotes;
    private long shares;
    private long bookMarks;
    private long downVotes;
    private long comments;

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public long getShares() {
        return shares;
    }

    public void setShares(long shares) {
        this.shares = shares;
    }

    public long getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(long bookMarks) {
        this.bookMarks = bookMarks;
    }

    public long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

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
