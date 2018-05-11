package com.learnexo.model.feed;

public class InterestFeed {
    private String interest;
    private String feedItemId;
    private String publisherId;
    private int feedType;

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getFeedItemId() {
        return feedItemId;
    }

    public void setFeedItemId(String feedItemId) {
        this.feedItemId = feedItemId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public String toString() {
        return "InterestFeed{" +
                "interest='" + interest + '\'' +
                ", feedItemId='" + feedItemId + '\'' +
                ", publisherId='" + publisherId + '\'' +
                ", feedType='" + feedType + '\'' +
                '}';
    }
}
