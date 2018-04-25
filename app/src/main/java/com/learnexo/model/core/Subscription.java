package com.learnexo.model.core;

import com.learnexo.model.video.Topic;

import java.util.Date;

public class Subscription {
    private Topic topic;
    private Date mPaidDate;
    private Date mExprireDate;
    private boolean mIsExpired;

    public Date getExprireDate() {
        return mExprireDate;
    }

    public void setExprireDate(Date exprireDate) {
        this.mExprireDate = exprireDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Date getPaidDate() {
        return mPaidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.mPaidDate = paidDate;
    }

    public boolean isExpired() {
        return mIsExpired;
    }

    public void setExpired(boolean expired) {
        this.mIsExpired = expired;
    }

}
