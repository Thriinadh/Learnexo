package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullAnswerActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_CONTENT = "com.learnexo.questiondata";
    public static final String EXTRA_CONTENT = "com.learnexo.answerdata";
    public static final String EXTRA_PUBLISHER_NAME = "com.learnexo.publisher_name";
    public static final String EXTRA_PUBLISHER_DP = "com.learnexo.publisher_dp";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_THUMBNAIL = "com.learnexo.imagepostedthumbnail";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";
    private static final String EXTRA_QUESTION_ID = "com.learnexo.questionId";

    private TextView viewAllAns;
    private TextView questionAsked;
    private TextView fullText;
    private ImageView postedImage;
    private ImageView challengeIcon;
    private TextView timeOfPost;
    private CircleImageView profileImage;
    private TextView userName;
    private String questionId;
    String questionData;
    boolean is_crack;
    String tag;
    String questionerId;
    private ImageView overFlowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_answer);


        wireViews();

        User publisher =new User();
        overFlowBtn.setOnClickListener(new PostAnsCrackItemOverflowListener(this, publisher));


        Intent intent=getIntent();
        is_crack = intent.getBooleanExtra("IS_CRACK", false);
        questionData = intent.getStringExtra(EXTRA_QUESTION_CONTENT);
        questionId = intent.getStringExtra(EXTRA_QUESTION_ID);
        String posTime = intent.getStringExtra(EXTRA_TIME);
        String postData = intent.getStringExtra(EXTRA_CONTENT);
        String imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        String imageThumb = intent.getStringExtra(EXTRA_THUMBNAIL);

        tag = intent.getStringExtra("TAG");
        questionerId = intent.getStringExtra("QUESTIONER_ID");


        bindData(intent, posTime, postData, imagePosted, imageThumb);

    }

    private void bindData(Intent intent, String posTime, String postData, String imagePosted, String imageThumb) {
        questionAsked.setText(questionData);
        fullText.setText(postData);
        userName.setText(intent.getStringExtra(EXTRA_PUBLISHER_NAME));
        timeOfPost.setText(posTime);
        if(is_crack){
            challengeIcon = findViewById(R.id.imageView5);
            challengeIcon.setVisibility(View.VISIBLE);
            viewAllAns.setText("View all cracks");
        }
        viewAllAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question question=new Question();
                question.setFeedItemId(questionId);
                question.setContent(questionData);
                question.setUserId(questionerId);
                question.setTags(Collections.singletonList(tag));
                Intent intent11 = AllAnswersActivity.newIntent(FullAnswerActivity.this, question);
                if(is_crack)
                    intent11.putExtra("ANSWER_TYPE", FeedItem.CRACK);
                else
                    intent11.putExtra("ANSWER_TYPE", FeedItem.ANSWER);
                startActivity(intent11);

            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(intent.getStringExtra(EXTRA_PUBLISHER_DP)).apply(requestOptions).into(profileImage);


        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(imagePosted)
                .thumbnail(Glide.with(getApplicationContext()).load(imageThumb))
                .apply(requestOptions).into(postedImage);
    }

    private void wireViews() {
        viewAllAns = findViewById(R.id.viewAllAns);
        questionAsked = findViewById(R.id.questionAsked);
        fullText = findViewById(R.id.full_text);
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.userNameTView);
        timeOfPost = findViewById(R.id.feed_time);
        postedImage = findViewById(R.id.postedImage);
        overFlowBtn = findViewById(R.id.imageView);
    }

    public static Intent newIntent(Context context, String questionAsked, String content, String publishedImg, String imageThumb, String timeAgo, User publisher, String questionId, String tag) {

        Intent intent = new Intent(context, FullAnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION_CONTENT, questionAsked);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        intent.putExtra(EXTRA_QUESTION_ID, questionId);
        intent.putExtra("TAG", tag);
        intent.putExtra("QUESTIONER_ID", publisher.getUserId());
        if(publishedImg!=null) {
            intent.putExtra(EXTRA_IMAGE, publishedImg);
            intent.putExtra(EXTRA_THUMBNAIL, imageThumb);
        }
        return intent;
    }
}
