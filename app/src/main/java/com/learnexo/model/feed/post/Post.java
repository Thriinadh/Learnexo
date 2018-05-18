package com.learnexo.model.feed.post;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.learnexo.model.feed.FeedItem;

@IgnoreExtraProperties
public class Post extends FeedItem {

    private long views;
    private int upvotes;
    private int shares;
    private int comments;
    private int bookMarks;
    private int downVotes;

    public Post() {}

    public Post(long views, int upvotes, int shares, int comments, int bookMarks, int downVotes) {
        this.views = views;
        this.upvotes = upvotes;
        this.shares = shares;
        this.comments = comments;
        this.bookMarks = bookMarks;
        this.downVotes = downVotes;
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


}
