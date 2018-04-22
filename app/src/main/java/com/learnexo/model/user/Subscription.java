package com.learnexo.model.user;

import com.learnexo.model.core.Topic;

import java.util.Date;

class Subscription {
    private Topic topic;
    private Date paid_date;
    private Date experiation_date;

    private boolean is_expired;

    public Date getExperiation_date() {
        return experiation_date;
    }

    public void setExperiation_date(Date experiation_date) {
        this.experiation_date = experiation_date;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Date getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(Date paid_date) {
        this.paid_date = paid_date;
    }

    public boolean isIs_expired() {
        return is_expired;
    }

    public void setIs_expired(boolean is_expired) {
        this.is_expired = is_expired;
    }

}
