package com.learnexo.model.user;

import com.learnexo.model.core.Subject;

import java.util.Date;

class Subscription {
    private Subject subject;
    private Date padi_date;
    private boolean is_expired;
    private boolean is_renewed;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getPadi_date() {
        return padi_date;
    }

    public void setPadi_date(Date padi_date) {
        this.padi_date = padi_date;
    }

    public boolean isIs_expired() {
        return is_expired;
    }

    public void setIs_expired(boolean is_expired) {
        this.is_expired = is_expired;
    }

    public boolean isIs_renewed() {
        return is_renewed;
    }

    public void setIs_renewed(boolean is_renewed) {
        this.is_renewed = is_renewed;
    }
}
