package com.learnexo.model.user;



import com.learnexo.model.core.AnsweredChallenge;
import com.learnexo.model.core.AnsweredQuestion;
import com.learnexo.model.core.AskedChallenge;
import com.learnexo.model.core.AskededQuestion;
import com.learnexo.model.core.Comment;
import com.learnexo.model.core.CreatedPost;
import com.learnexo.model.core.Like;
import com.learnexo.model.core.Share;


import java.util.List;

public class UserData {
    private UserId userId;

    private List<AnsweredQuestion> answeredQuestions;
    private List<AnsweredChallenge> answeredChallenges;
    private List<CreatedPost> createdPosts;

    private List<AskededQuestion> askededQuestions;
    private List<AskedChallenge> askedChallenges;



    //other
    private List<Like> likeList;
    private List<Comment> commentList;
    private List<Share> shareList;

    private List<Follower> followerList;
    private List<Subscription> subscriptionList;


}
