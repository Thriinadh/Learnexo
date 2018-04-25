package com.learnexo.model.core;

import com.learnexo.model.user.Follower;

import java.util.Date;
import java.util.List;

public class Answer {
    private String answer;
    private String questionId;
    private String questionName;

    private String userId;
    private String userName;
    private Date answeredDate;
    private List<String> imageUrls;
    private List<String> tagList;

    private List<Follower> followers;
    private boolean isCrack;

    private int noOfBookMarks;
    private int noOfViews;
    private int noOfUpvotes;
    private int noOfShares;
    private int noOfComments;
    private int noOfDownVotes;

    private boolean is_notifiable;
    private Report report;
}
