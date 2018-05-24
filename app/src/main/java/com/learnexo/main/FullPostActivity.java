package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.dao.PostDao;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;
import com.learnexo.util.RunInBackground;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullPostActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT = "com.learnexo.postdata";
    public static final String EXTRA_PUBLISHER_NAME = "com.learnexo.publisher_name";
    public static final String EXTRA_PUBLISHER_DP = "com.learnexo.publisher_dp";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_THUMB = "com.learnexo.imagepostedthumb";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";

    Boolean flag = true;
    private TextView fullText;
    private ImageView postedImage;
    private TextView timeOfPost;
    private TextView likesCount;
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView viewsText;
    private String publisherId;
    private long upVotes;

    private ImageView fullPostLikeBtn;
    private ImageView overFlowBtn;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        viewsText = findViewById(R.id.viewsText);
        likesCount = findViewById(R.id.likesCount);
        overFlowBtn = findViewById(R.id.imageView);
        User publisher =new User();
        overFlowBtn.setOnClickListener(new PostAnsCrackItemOverflowListener(this, publisher));


        fullPostLikeBtn = findViewById(R.id.full_post_like);

        setupToolbar();

        Intent intent=getIntent();
        publisherId=intent.getStringExtra("PUBLISHER_ID");
        long views=intent.getLongExtra("VIEWS",0);
        viewsText.setText(views+ " Views");
        final String postId=intent.getStringExtra("POST_ID");
        String postData = intent.getStringExtra(EXTRA_CONTENT);

        fullText = findViewById(R.id.full_text);
        fullText.setText(postData);

        profileImage = findViewById(R.id.profile_image);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(intent.getStringExtra(EXTRA_PUBLISHER_DP)).apply(requestOptions).into(profileImage);

        userName = findViewById(R.id.userNameTView);
        userName.setText(intent.getStringExtra(EXTRA_PUBLISHER_NAME));

        String posTime = intent.getStringExtra(EXTRA_TIME);
        timeOfPost = findViewById(R.id.feed_time);
        timeOfPost.setText(posTime);

        String imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        String imageThumb = intent.getStringExtra(EXTRA_THUMB);
        postedImage = findViewById(R.id.postedImage);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(imagePosted)
                .thumbnail(Glide.with(getApplicationContext()).load(imageThumb))
                .apply(requestOptions).into(postedImage);
        try {
            upVotes = (long) new RunInBackground().execute(publisherId, postId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        likesCount.setText(upVotes+" Up votes");
        Log.d("no of Upvotess", upVotes+" no of upVotesss");



        fullPostLikeBtn.setOnClickListener(new View.OnClickListener() {
            //color the like button and increase the likes in the UI
            //save new no of likes
            //store it in his activity log
            //generate edge rank
            //notify publisher
            //notify his followers

            @Override
            public void onClick(View view) {
                long upvotess=0;
                if(flag){
                    fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(FullPostActivity.this, R.drawable.post_likeblue_icon));
                    flag = false;
                        upvotess = upVotes +1;
                }else{
                        upvotess = upVotes;
                    fullPostLikeBtn.setImageDrawable(ContextCompat.getDrawable(FullPostActivity.this, R.drawable.post_like_icn));
                    flag = true;
                }

                Map<String, Object> map= new HashMap();
                map.put("upVotes",upvotess);
                likesCount.setText(upvotess+" Up votes");

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts").
                        document(postId).update(map);

            }
        });

    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Full post");
        }
    }

    public static Intent newIntent(Context context, String content, String publishedImg, String imageThumb, String timeAgo, User publisher, String postId, long viewss) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        intent.putExtra("POST_ID", postId);
        intent.putExtra("VIEWS",viewss);
        intent.putExtra("PUBLISHER_ID",publisher.getUserId());
        if(publishedImg!=null) {
            intent.putExtra(EXTRA_IMAGE, publishedImg);
            intent.putExtra(EXTRA_THUMB, imageThumb);
        }
        return intent;
    }
}
