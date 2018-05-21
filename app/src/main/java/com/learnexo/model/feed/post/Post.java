package com.learnexo.model.feed.post;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.learnexo.model.feed.FeedItem;

@IgnoreExtraProperties
public class Post extends FeedItem {

    private long views;
    private long upVotes;
    private long shares;
    private long comments;
    private long bookMarks;
    private long downVotes;

    public Post() {}

    public Post(long views, int upVotes, int shares, int comments, int bookMarks, int downVotes) {
        this.views = views;
        this.upVotes = upVotes;
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

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
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
}
