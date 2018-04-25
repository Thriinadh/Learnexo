package com.learnexo.model.user;

import com.learnexo.model.core.answer.AnswerBookMark;
import com.learnexo.model.core.answer.AnswerComment;
import com.learnexo.model.core.answer.AnswerDownVote;
import com.learnexo.model.core.answer.AnswerShare;
import com.learnexo.model.core.answer.AnswerUpVote;
import com.learnexo.model.core.post.PostBookMark;
import com.learnexo.model.core.post.PostComment;
import com.learnexo.model.core.post.PostDownVote;
import com.learnexo.model.core.post.PostShare;
import com.learnexo.model.core.post.PostUpVote;
import com.learnexo.model.core.question.QuestionBookMark;
import com.learnexo.model.core.question.QuestionComment;
import com.learnexo.model.core.question.QuestionDownVote;
import com.learnexo.model.core.question.QuestionShare;
import com.learnexo.model.core.question.QuestionUpVote;

import java.util.List;

public class ActivityLog {
    private String userId;

    private List<PostUpVote> postUpVotes;
    private List<PostComment> postComments;
    private List<PostShare> postShares;
    private List<PostBookMark> postBookmarks;
    private List<PostDownVote> postDownVotes;


    private List<QuestionUpVote> quesUpVotes;
    private List<QuestionComment> quesComments;
    private List<QuestionShare> quesShares;
    private List<QuestionBookMark> quesBookMarks;
    private List<QuestionDownVote> quesDownVotes;

    private List<AnswerUpVote> ansUpVotes;
    private List<AnswerComment> ansComments;
    private List<AnswerShare> ansShares;
    private List<AnswerBookMark> ansBookMarks;
    private List<AnswerDownVote> ansDownVotes;



}
