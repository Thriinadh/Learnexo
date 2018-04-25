package com.learnexo.model.user;

//T can be Post, Question, Answer or User
public class Follower<T> extends User{
    private T type;
    private String tFollowerId;

}
