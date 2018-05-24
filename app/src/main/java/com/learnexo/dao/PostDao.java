package com.learnexo.dao;

import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;



public class PostDao {
    private static FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private Post mPost;

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }



}
