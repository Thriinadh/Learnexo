package com.learnexo.model.user.profile;

import com.learnexo.model.core.question.AnsweredQuestion;
import com.learnexo.model.core.question.AskededQuestion;

import java.util.List;

public class UserQuestions {
    private String userId;
    private List<AnsweredQuestion> answeredQuestions;
    private List<AskededQuestion> askededQuestions;
}
