package com.learnexo.model.user;

import com.learnexo.model.feed.answer.AnswerBookMark;
import com.learnexo.model.feed.answer.AnswerComment;
import com.learnexo.model.feed.answer.AnswerDownVote;
import com.learnexo.model.feed.answer.AnswerShare;
import com.learnexo.model.feed.answer.AnswerUpVote;
import com.learnexo.model.feed.post.PostBookMark;
import com.learnexo.model.feed.post.PostComment;
import com.learnexo.model.feed.post.PostDownVote;
import com.learnexo.model.feed.post.PostShare;
import com.learnexo.model.feed.post.PostUpVote;
import com.learnexo.model.feed.question.QuestionBookMark;
import com.learnexo.model.feed.question.QuestionComment;
import com.learnexo.model.feed.question.QuestionDownVote;
import com.learnexo.model.feed.question.QuestionShare;
import com.learnexo.model.feed.question.QuestionUpVote;

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
