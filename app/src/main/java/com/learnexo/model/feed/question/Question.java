package com.learnexo.model.feed.question;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

//only used for unanswered questions
@IgnoreExtraProperties
public class Question {
    private String question;

    @ServerTimestamp
    private Date askTime;
    private String userId;
    private String userName;

    private String imgUrl;
    private String imgThmb;
    private List<String> tags;

    private boolean is_notifiable;
    private boolean isChallenge;

    public Question() {}

    public Question(String question, Date askTime, String userId, String userName, String imgUrl, String imgThmb, List<String> tags, boolean is_notifiable, boolean isChallenge) {
        this.question = question;
        this.askTime = askTime;
        this.userId = userId;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.imgThmb = imgThmb;
        this.tags = tags;
        this.is_notifiable = is_notifiable;
        this.isChallenge = isChallenge;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Date getAskTime() {
        return askTime;
    }

    public void setAskTime(Date askTime) {
        this.askTime = askTime;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isIs_notifiable() {
        return is_notifiable;
    }

    public void setIs_notifiable(boolean is_notifiable) {
        this.is_notifiable = is_notifiable;
    }

    public boolean isChallenge() {
        return isChallenge;
    }

    public void setChallenge(boolean challenge) {
        isChallenge = challenge;
    }
}
