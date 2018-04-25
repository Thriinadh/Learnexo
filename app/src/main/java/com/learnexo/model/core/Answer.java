package com.learnexo.model.core;

import java.util.Date;
import java.util.List;

public class Answer {
    private String answer;
    private Date answeredDate;

    private String questionId;
    private String questionName;

    private String userId;
    private String userName;

    private List<String> imageUrls;
    private List<String> tagList;

    private boolean isCrack;

    private int noOfBookMarks;
    private int noOfViews;
    private int noOfUpvotes;
    private int noOfShares;
    private int noOfComments;
    private int noOfDownVotes;

    private boolean is_notifiable;
}
