package com.learnexo.model.feed;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class FeedItem {

    private String content;
    public static final int POST=0;
    public static final int ANSWER =1;
    public static final int CRACK =2;
    public static final int NO_ANS_QUES=3;
    public static final int NO_CRACK_CHALLENGE =4;
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @ServerTimestamp
    private Date publishTime;

    private String userId;
    private String userName;

    private String imgUrl;
    private String imgThmb;

    private boolean is_notifiable;
    private List<String> tags;

    public FeedItem() {}

    public FeedItem(String content, Date mPublishTime, String userId, String userName, String imgUrl, String imgThmb, boolean is_notifiable, List<String> tags) {
        this.content = content;
        this.publishTime = mPublishTime;
        this.userId = userId;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.imgThmb = imgThmb;

        this.is_notifiable = is_notifiable;
        this.tags = tags;
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
}
