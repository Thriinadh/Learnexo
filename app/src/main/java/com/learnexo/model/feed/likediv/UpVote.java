package com.learnexo.model.feed.likediv;

import java.util.Date;

public class UpVote {
    private String feedItemId;
    private String upVoterId;
    private String publisherId;
    private Date upvoteTime;

    public String getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(String feedItemId) {
        this.feedItemId = feedItemId;
    }

    public String getUpVoterId() {
        return upVoterId;
    }

    public void setUpVoterId(String upVoterId) {
        this.upVoterId = upVoterId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public Date getUpvoteTime() {
        return upvoteTime;
    }

    public void setUpvoteTime(Date upvoteTime) {
        this.upvoteTime = upvoteTime;
    }
}
