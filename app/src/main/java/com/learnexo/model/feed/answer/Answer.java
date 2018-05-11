package com.learnexo.model.feed.answer;

import com.learnexo.model.feed.FeedItem;

public class Answer extends FeedItem {
    private String quesId;
    private String quesName;

    private boolean isCrack;

    private int bookMarks;
    private long views;

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getQuesName() {
        return quesName;
    }

    public void setQuesName(String quesName) {
        this.quesName = quesName;
    }

    public boolean isCrack() {
        return isCrack;
    }

    public void setCrack(boolean crack) {
        isCrack = crack;
    }

    public int getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(int bookMarks) {
        this.bookMarks = bookMarks;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    private int upvotes;
    private int shares;
    private int comments;
    private int downVotes;


}
