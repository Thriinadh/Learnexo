package com.learnexo.main;

import android.app.Activity;
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

    public LikeBtnListener(ImageView fullPostLikeBtn, TextView likesCount, boolean flag, String publisherId, long upVotes, String postId, Activity activity) {
        this.fullPostLikeBtn = fullPostLikeBtn;
        this.likesCount = likesCount;
        this.flag = flag;
        this.publisherId = publisherId;
        this.upVotes = upVotes;
        this.postId = postId;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        long upvotess;
        if(flag){
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_likeblue_icon));
            flag = false;
            upvotess = upVotes +1;
        }else{
            upvotess = upVotes;
            fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.post_like_icn));
            flag = true;
        }

        Map<String, Object> map= new HashMap();
        map.put("upVotes",upvotess);
        likesCount.setText(upvotess+" Up votes");

        mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts").
                document(postId).update(map);

    }
}
