package com.learnexo.model.feed.post;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.learnexo.model.feed.FeedItem;

@IgnoreExtraProperties
public class Post extends FeedItem {

    private long views;
    private long upVotes;
    private long shares;
    private long bookMarks;
    private long downVotes;

    public Post() {}

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
