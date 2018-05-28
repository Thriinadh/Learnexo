package com.learnexo.model.feed.likediv;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private String itemId;
    private String commenterId;
    private String publisherId;
    private String comment;
    private @ServerTimestamp Date commentTime;

    public Comment() {}

    public Comment(String itemId, String commenterId, String publisherId, String comment, Date commentTime) {
        this.itemId = itemId;
        this.commenterId = commenterId;
        this.publisherId = publisherId;
        this.comment = comment;
        this.commentTime = commentTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
