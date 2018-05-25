package com.learnexo.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;


public class LikeBtnListener implements View.OnClickListener {
    FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private ImageView fullPostLikeBtn;
    private TextView likesCount;

    private boolean flag;
    private String publisherId;

    private long upVotes;
    private String postId;
    private Activity mActivity;
    private boolean isAnswer;

    public LikeBtnListener(ImageView fullPostLikeBtn, TextView likesCount, boolean flag, String publisherId,
                           String postId, long upVotes, Activity activity, boolean isAnswer) {
        this.fullPostLikeBtn = fullPostLikeBtn;
        this.likesCount = likesCount;
        this.flag = flag;
        this.upVotes=upVotes;
        this.publisherId = publisherId;
        this.postId = postId;
        this.isAnswer=isAnswer;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        long upvotess;
        if(flag){
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_likeblue_icon));
            upvotess = upVotes +1;
            flag = false;
        }else{
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_like_icn));
            upvotess = upVotes;
            flag = true;
        }
        if(upvotess!=1)
            likesCount.setText(upvotess +" Up votes");
        else
            likesCount.setText("1 Up vote");

        final Map<String, Object> map= new HashMap();
        map.put("upVotes", upvotess);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String path;
                if(isAnswer)
                    path = "answers";
                else
                    path = "posts";

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection(path).
                        document(postId).update(map);
            }
        });
    }
}
