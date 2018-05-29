package com.learnexo.model.feed;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class FeedItem {

    public static final int POST=0;
    public static final int ANSWER =1;
    public static final int CRACK =2;
    public static final int QUESTION =3;
    public static final int CHALLENGE =4;

    private String feedItemId;
    private String content;

    @ServerTimestamp
    private Date publishTime;

    private String userId;
    private String userName;
    private String imgUrl;
    private String imgThmb;

    public int type;
    private int edgeRank;
    private long comments;
    private List<String> tags;
    private boolean is_notifiable;


    public FeedItem() {}

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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



    public String getFeedItemId() {
        return feedItemId;
    }
    public void setFeedItemId(String feedItemId) {
        this.feedItemId = feedItemId;
    }

    public int getEdgeRank() {
        return edgeRank;
    }

    public void setEdgeRank(int edgeRank) {
        this.edgeRank = edgeRank;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
