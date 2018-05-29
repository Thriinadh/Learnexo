package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.likediv.Comment;
import com.learnexo.model.feed.post.PostDetails;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullPostActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT = "com.learnexo.postdata";
    public static final String EXTRA_PUBLISHER_NAME = "com.learnexo.publisher_name";
    public static final String EXTRA_PUBLISHER_DP = "com.learnexo.publisher_dp";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_THUMB = "com.learnexo.imagepostedthumb";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";

    private List<Comment> mComments;
    private UserCommentsRecyclerAdapter mAdapter;

    private TextView fullText;
    private ImageView postedImage;
    private TextView timeOfPost;
    private TextView likesCount;
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView userName;
    private TextView viewsText;
    private boolean flag = true;
    private String publisherId;
    private long upVotes;
    private long views;
    private String postId;
    private String postData;
    private CircleImageView commentsImage;
    private TextView commentBtn;
    private RecyclerView commentsRecycler;
    private TextView seeAllComments;

    private String publisherDP;
    private String publisherName;

    private ImageView fullPostLikeBtn;
    private ImageView overFlowBtn;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        wireViews();
        setupToolbar();

        mComments = new ArrayList<>();
        mAdapter = new UserCommentsRecyclerAdapter(mComments);

        commentsRecycler = findViewById(R.id.commentsRecycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(FullPostActivity.this));
        commentsRecycler.setAdapter(mAdapter);

        Intent intent=getIntent();
        publisherId=intent.getStringExtra("PUBLISHER_ID");
        postId = intent.getStringExtra("POST_ID");
        postData = intent.getStringExtra(EXTRA_CONTENT);
        String imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        String imageThumb = intent.getStringExtra(EXTRA_THUMB);
        publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAME);
        String posTime = intent.getStringExtra(EXTRA_TIME);
        publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DP);

        final User publisher = new User(publisherId, publisherName, publisherDP);


        bindData(imagePosted, imageThumb, publisherName, posTime, publisherDP);

        String path="posts";
        PostDetails postDetails = mFirebaseUtil.getViewsUpvotes(publisherId, postId, path);
        bindViewsUpvotes(postDetails);

        overFlowBtn.setOnClickListener(new PostAnsCrackItemOverflowListener(this, publisher));

        profileListener(publisher);

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FullPostActivity.this, CommentsActivity.class);
                intent1.putExtra("EXTRA_PUBLISHER_IDDD", publisherId);
                intent1.putExtra("EXTRA_POST_ITEM_ID", postId);
                startActivity(intent1);
            }
        });

        seeAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeAllComments.setEnabled(false);

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts").document(postId).collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: documents) {
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            mComments.add(comment);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        seeAllComments.setEnabled(true);
    }


    private void profileListener(final User publisher) {
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = OthersProfileActivity.newIntent(FullPostActivity.this, publisher);
                startActivity(intent1);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = OthersProfileActivity.newIntent(FullPostActivity.this, publisher);
                startActivity(intent1);
            }
        });
    }

    private void bindViewsUpvotes(PostDetails postDetails) {
        try {
            upVotes=postDetails.getNoOfLikes();
            views=postDetails.getNoOfViews();

            //color the like button and increase the likes in the UI
            //save new no of likes
            //store it in his activity log
            //generate edge rank
            //notify publisher
            //notify his followers
            fullPostLikeBtn.setOnClickListener(

                    new LikeBtnListener(fullPostLikeBtn,likesCount,flag, publisherId,postId, upVotes,FullPostActivity.this, false, null)
            );

            likesCount.setText(upVotes+" Up votes");
            if(views==0){
                views=1;
                viewsText.setText("1 View");
            }else{
                viewsText.setText(views+ " Views");
            }


            long viewss = views+1;
            Map<String, Object> map= new HashMap();
            map.put("views",viewss);

            mFirebaseUtil.mFirestore.collection("users").
                    document(publisherId).
                    collection("posts").
                    document(postId).update(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData(String imagePosted, String imageThumb, String publisherName, String posTime, String publisherDP) {
        fullText.setText(postData);
        userName.setText(publisherName);
        timeOfPost.setText(posTime);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(publisherDP).apply(requestOptions).into(profileImage);


        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(imagePosted)
                .thumbnail(Glide.with(getApplicationContext()).load(imageThumb))
                .apply(requestOptions).into(postedImage);

    }

    private void wireViews() {
        viewsText = findViewById(R.id.viewsText);
        likesCount = findViewById(R.id.likesCount);
        fullPostLikeBtn = findViewById(R.id.full_post_like);
        overFlowBtn = findViewById(R.id.imageView);
        fullText = findViewById(R.id.full_text);
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.userNameTView);
        timeOfPost = findViewById(R.id.feed_time);
        postedImage = findViewById(R.id.postedImage);

        seeAllComments = findViewById(R.id.seeAllComments);
        commentsImage = findViewById(R.id.commentsImage);
        commentBtn = findViewById(R.id.commentBtn);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(FeedFragment.sDpUrl).apply(requestOptions).into(commentsImage);
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Full Post");
        }
    }

    public static Intent newIntent(Context context, String content, String publishedImg, String imageThumb, String timeAgo, User publisher, String postId) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        intent.putExtra("POST_ID", postId);
        intent.putExtra("PUBLISHER_ID",publisher.getUserId());
        if(publishedImg!=null) {
            intent.putExtra(EXTRA_IMAGE, publishedImg);
            intent.putExtra(EXTRA_THUMB, imageThumb);
        }
        return intent;
    }

}
