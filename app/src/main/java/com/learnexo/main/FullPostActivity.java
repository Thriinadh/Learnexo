package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private TextView fullText;
    private ImageView postedImage;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView timeOfPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        String postData = getIntent().getStringExtra(EXTRA_CONTENT);
        fullText = findViewById(R.id.full_text);
        fullText.setText(postData);

        String imagePosted = getIntent().getStringExtra(EXTRA_IMAGE);
        postedImage = findViewById(R.id.postedImage);
//        if(imagePosted != null)
//        postedImage.setImageURI(Uri.parse(imagePosted));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(FullPostActivity.this).load(imagePosted).apply(requestOptions).into(postedImage);
    }

    public static Intent newIntent(Context context, String postData, String imagePosted) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,postData);
        intent.putExtra(EXTRA_IMAGE, imagePosted);
        return intent;
    }
}
