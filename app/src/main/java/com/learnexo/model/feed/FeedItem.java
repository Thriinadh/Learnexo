package com.learnexo.model.feed;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class FeedItem {

    private String content;

    @ServerTimestamp
    private Date posTime;

    private String userId;//publisher
    private String userName;

    private String imgUrl;
    private String imgThmb;

    private long views;
    private int upvotes;
    private int shares;
    private int comments;
    private int bookMarks;
    private int downVotes;

    private boolean is_notifiable;

    private List<String> tags;
    public FeedItem() {}

    public FeedItem(String content, Date posTime, String userId, String userName, String imgUrl, String imgThmb, long views, int upvotes, int shares, int comments, int bookMarks, int downVotes, boolean is_notifiable, List<String> tags) {
        this.content = content;
        this.posTime = posTime;
        this.userId = userId;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.imgThmb = imgThmb;
        this.views = views;
        this.upvotes = upvotes;
        this.shares = shares;
        this.comments = comments;
        this.bookMarks = bookMarks;
        this.downVotes = downVotes;
        this.is_notifiable = is_notifiable;
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPosTime() {
        return posTime;
    }

    public void setPosTime(Date posTime) {
        this.posTime = posTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgThmb() {
        return imgThmb;
    }

    public void setImgThmb(String imgThmb) {
        this.imgThmb = imgThmb;
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

    public int getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(int bookMarks) {
        this.bookMarks = bookMarks;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public boolean isIs_notifiable() {
        return is_notifiable;
    }

    public void setIs_notifiable(boolean is_notifiable) {
        this.is_notifiable = is_notifiable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
