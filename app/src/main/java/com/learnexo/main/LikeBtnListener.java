package com.learnexo.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnexo.util.FirebaseUtil;
import com.learnexo.util.MyBounceInterpolator;

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
    private String questionId;

    public LikeBtnListener(ImageView fullPostLikeBtn, TextView likesCount, boolean flag, String publisherId,
                           String postId, long upVotes, Activity activity, boolean isAnswer, String questionId) {
        this.fullPostLikeBtn = fullPostLikeBtn;
        this.likesCount = likesCount;
        this.flag = flag;
        this.upVotes=upVotes;
        this.publisherId = publisherId;
        this.postId = postId;
        this.isAnswer=isAnswer;
        mActivity = activity;
        this.questionId=questionId;
    }

    @Override
    public void onClick(View view) {

        Animation myAnim = AnimationUtils.loadAnimation(mActivity, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
        myAnim.setInterpolator(interpolator);


        if(flag){
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_likeblue_icon));
            upVotes = upVotes +1;
            flag = false;
        }else{
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_like_icn));
            upVotes = upVotes;
            flag = true;
        }
        if(upVotes!=1)
            likesCount.setText(upVotes +" Up votes");
        else
            likesCount.setText("1 Up vote");

        final Map<String, Object> map= new HashMap();
        map.put("upVotes", upVotes);

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

                if(path.equals("answers")&&questionId!=null)
                mFirebaseUtil.mFirestore.collection("questions").document(questionId).collection(path).
                        document(postId).update(map);
            }
        });
    }
}
