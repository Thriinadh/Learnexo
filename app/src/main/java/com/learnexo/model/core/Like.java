package com.learnexo.model.core;

import com.learnexo.model.user.UserId;

import java.util.Date;
import java.util.UUID;

public class Like{
    private UUID postId;//should be unique across posts, questions & challenges
    private UserId userId;
    private Date likedTime;

}
