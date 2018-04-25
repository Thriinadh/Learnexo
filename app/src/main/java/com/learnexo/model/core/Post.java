package com.learnexo.model.core;

import java.util.Date;
import java.util.List;

public class Post {
    private String postContent;

    private String userId;
    private String userName;

    private Date postedDate;
    private List<String> imageUrls;
    private List<String> tagList;

    private int noOfBookMarks;
    private int noOfViews;
    private int noOfUpvotes;
    private int noOfShares;
    private int noOfComments;
    private int noOfDownVotes;

    private boolean is_notifiable;
    private Report report;


}
