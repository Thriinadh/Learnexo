package com.learnexo.model.user;

import java.util.List;

//T can be Post, Question or User
public class Followers<T> {
    private List<Follower<T>> tFollowers;
}
