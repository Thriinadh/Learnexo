package com.learnexo.model.user;

import java.util.List;

//T can be Post, Question, Answer or User
public class Followers<T> {
    private List<Follower<T>> tFollowers;
}
