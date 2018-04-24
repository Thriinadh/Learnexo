package com.learnexo.model.core;

import com.learnexo.model.user.UserId;

import java.util.Date;
import java.util.UUID;

public class Bookmark {
    private String postId;//should be unique across posts, questions & challenges
    private UserId userId;
    private Date likedTime;
}
