package com.learnexo.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnexo.model.feed.FeedItem;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;


public class UpVoteListener implements View.OnClickListener {
    FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private ImageView mUpVoteBtn;
    private TextView mUpVoteView;

    private boolean flag;
    private String publisherId;

    private long upVotes;
    private String mFeedItemId;
    private Activity mActivity;
    private boolean isAnswer;
    private boolean isCrack;

    private String questionId;
    private String mCurrentUserId=FirebaseUtil.getCurrentUserId();

    public UpVoteListener(ImageView mUpVoteBtn, TextView upVoteView, boolean flag, String publisherId,
                          String mFeedItemId, long upVotes, Activity activity, boolean isAnswer, String questionId, boolean isCrack) {
        this.mUpVoteBtn = mUpVoteBtn;
        this.mUpVoteView = upVoteView;
        this.flag = flag;
        this.upVotes=upVotes;
        this.publisherId = publisherId;
        this.mFeedItemId = mFeedItemId;
        this.questionId=questionId;
        this.isAnswer=isAnswer;
        this.isCrack=isCrack;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(0);
        anim.setDuration(300);

        mUpVoteBtn.startAnimation(anim);

        if(flag){
            mUpVoteBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_likeblue_icon));
            upVotes = upVotes +1;
            flag = false;
        } else{
              mUpVoteBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_like_icn));
              upVotes = upVotes - 1;
              flag = true;
        }
        if(upVotes!=1)
            mUpVoteView.setText(upVotes +" Up votes");
        else
            mUpVoteView.setText("1 Up vote");

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

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection(path).document(mFeedItemId).update(map);

                if(path.equals("answers")&&questionId!=null)
                    mFirebaseUtil.mFirestore.collection("questions").document(questionId).collection(path).document(mFeedItemId).update(map);

                Map<String, Object> map1= new HashMap();
                map1.put("publisherId",publisherId);
                if(isAnswer) {
                    if(isCrack)
                        map1.put("feedItemType", FeedItem.CRACK);
                    else
                        map1.put("feedItemType", FeedItem.ANSWER);
                }else
                    map1.put("feedItemType", FeedItem.POST);

                mFirebaseUtil.mFirestore.collection("users").document(mCurrentUserId).collection("up_votes").document(mFeedItemId).set(map1);
            }
        });
    }
}
