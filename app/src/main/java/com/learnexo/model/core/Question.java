package com.learnexo.model.core;

import com.learnexo.model.user.Follower;
import com.learnexo.model.user.RequestedUser;
import com.learnexo.model.user.User;

import java.util.Date;
import java.util.List;

//only used for unanswered questions, list of answer ids
public class Question {
    private String question;
    private String userId;
    private Date postedDate;
    private List<String> answerIdList;

    private List<String> imageUrls;
    private List<String> tagList;

    private List<User> requestedUsers;
    private List<Follower> followerList;
    private boolean is_notifiable;
    private boolean isChallenge;
}
