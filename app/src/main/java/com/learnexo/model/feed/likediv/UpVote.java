package com.learnexo.model.feed.likediv;

import com.learnexo.model.core.BookMarkType;

import java.util.Date;

public class UpVote {
    private String feedItemId;
    private String upVoterId;
    private String publisherId;
    private Date upvoteTime;
    private BookMarkType upVoteType;

    public UpVote(String feedItemId, String publisherId, BookMarkType upVoteType) {
        this.feedItemId = feedItemId;
        this.publisherId = publisherId;
        this.upVoteType = upVoteType;
    }

    public UpVote(){}

    public BookMarkType getUpVoteType() {
        return upVoteType;
    }

    public void setUpVoteType(BookMarkType upVoteType) {
        this.upVoteType = upVoteType;
    }

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


    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Bookmark)) {
            return false;
        }

        UpVote upVote = (UpVote) o;

        return upVote.feedItemId.equals(feedItemId) &&
                upVote.upVoteType.name().equals(upVoteType.name()) &&
                upVote.publisherId.equals(publisherId);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + feedItemId.hashCode();
        result = 31 * result + upVoteType.name().hashCode();
        result = 31 * result + publisherId.hashCode();
        return result;
    }
}
