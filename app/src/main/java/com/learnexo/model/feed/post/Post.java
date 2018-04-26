package com.learnexo.model.feed.post;

import java.util.Date;
import java.util.List;

public class Post {
    private String content;
    private Date posTime;

    private String userId;//publisher
    private String userName;

    private String imgUrl;
    private String imgThmb;

    private long views;
    private int upvotes;
    private int shares;
    private int comments;
    private int bookMarks;
    private int downVotes;

    private boolean is_notifiable;

    private List<String> tags;
}
