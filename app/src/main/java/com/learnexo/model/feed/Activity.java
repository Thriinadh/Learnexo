package com.learnexo.model.feed;

import com.learnexo.model.core.ActivityType;

import java.util.Date;

public class Activity {
    private String userId;
    private String feedItemId;
    private ActivityType mActivityType;
    private Date time;

    private long edgeRank;
}
