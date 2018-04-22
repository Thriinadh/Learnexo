package com.learnexo.model.feed;


import java.util.Date;

public class FeedSharePostModel {

    private String user_id, image_url, postedContent, subject, image_thumb;

    private Date timestamp;

    public FeedSharePostModel() {}

    public FeedSharePostModel(String user_id, String image_url, String postedContent, String subject, String image_thumb, Date timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.postedContent = postedContent;
        this.subject = subject;
        this.image_thumb = image_thumb;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPostedContent() {
        return postedContent;
    }

    public void setPostedContent(String postedContent) {
        this.postedContent = postedContent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
