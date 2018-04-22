package com.learnexo.model.user;

import com.learnexo.model.core.AnsweredQuestion;
import com.learnexo.model.core.AskededQuestion;

import java.util.List;

public class UserQuestions {
    private UserId userId;
    private List<AnsweredQuestion> answeredQuestions;
    private List<AskededQuestion> askededQuestions;
}
