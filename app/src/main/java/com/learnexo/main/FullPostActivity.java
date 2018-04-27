package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullPostActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT = "com.learnexo.postdata";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_PROFILE_IMAGE = "com.learnexo.profileimage";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";

    private TextView fullText;
    private ImageView postedImage;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView timeOfPost;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        setupToolbar();

        String postData = getIntent().getStringExtra(EXTRA_CONTENT);
        fullText = findViewById(R.id.full_text);
        fullText.setText(postData);

        String posTime = getIntent().getStringExtra(EXTRA_TIME);
        timeOfPost = findViewById(R.id.feed_time);
        timeOfPost.setText(posTime);

        String imagePosted = getIntent().getStringExtra(EXTRA_IMAGE);
        postedImage = findViewById(R.id.postedImage);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(FullPostActivity.this).load(imagePosted).apply(requestOptions).into(postedImage);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Full post");
        }
    }

    public static Intent newIntent(Context context, String postData, String imagePosted, String posTime) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,postData);
        intent.putExtra(EXTRA_IMAGE, imagePosted);
        intent.putExtra(EXTRA_TIME, posTime);
        return intent;
    }
}
