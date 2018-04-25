package com.learnexo.model.core;

import java.util.Date;
import java.util.List;

//only used for unanswered questions, list of answer ids
public class Question {
    private String question;
    private Date askedDate;
    private String userId;

    private List<String> answerIds;
    private List<String> imageUrls;
    private List<String> tagList;

    private boolean is_notifiable;
    private boolean isChallenge;
}
