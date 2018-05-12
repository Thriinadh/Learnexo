package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.model.user.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullPostActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT = "com.learnexo.postdata";
    public static final String EXTRA_PUBLISHER_NAME = "com.learnexo.publisher_name";
    public static final String EXTRA_PUBLISHER_DP = "com.learnexo.publisher_dp";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";

    private TextView fullText;
    private ImageView postedImage;
    private TextView timeOfPost;
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        setupToolbar();

        Intent intent=getIntent();
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
        postedImage = findViewById(R.id.postedImage);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(imagePosted).apply(requestOptions).into(postedImage);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Full post");
        }
    }

    public static Intent newIntent(Context context, String content, String publishedImg, String timeAgo, User publisher) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        if(publishedImg!=null)
            intent.putExtra(EXTRA_IMAGE, publishedImg);
        return intent;
    }
}
