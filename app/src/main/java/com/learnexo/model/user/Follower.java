package com.learnexo.model.user;

//T can be Post, Question or User
public class Follower<T> extends User{
    private T type;
    private String tFollowerId;

}
