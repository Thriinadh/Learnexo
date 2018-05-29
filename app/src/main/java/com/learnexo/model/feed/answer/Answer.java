package com.learnexo.model.feed.answer;

import com.learnexo.model.feed.FeedItem;

public class Answer extends FeedItem {
    private String quesId;
    private String questionerId;
    private String quesContent;
    private boolean isCrack;

    private long bookMarks;
    private long views;
    private long upVotes;
    private long shares;

    private long downVotes;

    public Answer() {}

    public String getQuesId() {
        return quesId;
    }

    public void setQuesId(String quesId) {
        this.quesId = quesId;
    }

    public String getQuesContent() {
        return quesContent;
    }

    public void setQuesContent(String quesContent) {
        this.quesContent = quesContent;
    }

    public String getQuestionerId() {
        return questionerId;
    }
    public void setQuestionerId(String questionerId) {
        this.questionerId = questionerId;
    }

    public boolean isCrack() {
        return isCrack;
    }

    public void setCrack(boolean crack) {
        isCrack = crack;
    }



    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(long bookMarks) {
        this.bookMarks = bookMarks;
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

    public long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }
}
