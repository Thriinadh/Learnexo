package com.learnexo.model.feed.likediv;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private String feedItemId;
    private String commenterId;
    private String publisherId;
    private String comment;
    private @ServerTimestamp Date commentTime;
    private String commenterName;
    private String commenterDp;

    public Comment() {}

    public Comment(String feedItemId, String commenterId, String publisherId, String comment, Date commentTime, String commenterName, String commenterDp) {
        this.feedItemId = feedItemId;
        this.commenterId = commenterId;
        this.publisherId = publisherId;
        this.comment = comment;
        this.commentTime = commentTime;
        this.commenterName = commenterName;
        this.commenterDp = commenterDp;
    }

    public String getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(String feedItemId) {
        this.feedItemId = feedItemId;
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

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommenterDp() {
        return commenterDp;
    }

    public void setCommenterDp(String commenterDp) {
        this.commenterDp = commenterDp;
    }
}
