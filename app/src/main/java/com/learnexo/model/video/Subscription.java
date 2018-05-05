package com.learnexo.model.video;

import java.util.Date;

public class Subscription {
    private Subject mSubject;
    private Date mPaidDate;
    private Date mExprireDate;
    private boolean mIsExpired;

    public Date getExprireDate() {
        return mExprireDate;
    }

    public void setExprireDate(Date exprireDate) {
        this.mExprireDate = exprireDate;
    }

    public Subject getSubject() {
        return mSubject;
    }

    public void setSubject(Subject subject) {
        this.mSubject = subject;
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
