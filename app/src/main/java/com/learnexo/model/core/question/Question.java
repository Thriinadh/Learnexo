package com.learnexo.model.core.question;

import java.util.Date;
import java.util.List;

//only used for unanswered questions
public class Question {
    private String question;
    private Date askedDate;
    private String userId;

    private List<String> imageUrls;
    private List<String> tagList;

    private boolean is_notifiable;
    private boolean isChallenge;
}
