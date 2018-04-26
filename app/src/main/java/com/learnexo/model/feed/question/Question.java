package com.learnexo.model.feed.question;

import java.util.Date;
import java.util.List;

//only used for unanswered questions
public class Question {
    private String question;
    private Date askTime;
    private String userId;

    private String imgUrl;
    private String imgThmb;
    private List<String> tags;

    private boolean is_notifiable;
    private boolean isChallenge;
}
