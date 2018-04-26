package com.learnexo.model.feed.answer;

import java.util.Date;
import java.util.List;

public class Answer {
    private String answer;
    private Date ansTime;

    private String quesId;
    private String quesName;

    private String userId;//answerer id
    private String userName;//answerer name

    private String imgUrl;
    private String imgThmb;
    private List<String> tags;

    private boolean isCrack;//tell whether it is a challenge

    private int bookMarks;
    private long views;
    private int upvotes;
    private int shares;
    private int comments;
    private int downVotes;

    private boolean is_notifiable;
}
