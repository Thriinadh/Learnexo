package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.core.BookMarkType;
import com.learnexo.model.feed.likediv.Bookmark;
import com.learnexo.model.feed.likediv.Comment;
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
    public static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    public static final String EXTRA_THUMB = "com.learnexo.imagepostedthumb";
    public static final String EXTRA_TIME = "com.learnexo.postedtime";

    private List<Comment> mComments;
    private CommentsAdapter mAdapter;

    private TextView fullText;
    private TextView timeOfPost;
    private TextView likesCount;
    private TextView userName;
    private TextView viewsText;
    private TextView seeAllComments;
    private TextView commentBtn;

    private Toolbar toolbar;
    private long upVotes;
    private long views;
    private long comments;
    private boolean flag = true;

    private CircleImageView profileImage;
    private CircleImageView commentsImage;
    private RecyclerView commentsRecycler;

    private String publisherId;
    private String postId;
    private String postData;
    private String publisherDP;
    private String publisherName;
    private String imageThumb;
    private String posTime;
    private String imagePosted;

    private ImageView postedImage;
    private ImageView fullPostLikeBtn;
    private ImageView overFlowBtn;

    private ImageView full_post_bookmark;
    String bookMarkItemIdd;
    boolean gag = true;

    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);
        handleIntent();

        wireViews();
        setupToolbar();

        setupCommentsRecycler();

        final User publisher = new User(publisherId, publisherName, publisherDP);

        bindData(imagePosted, imageThumb, publisherName, posTime, publisherDP);

        bindViewsUpvotes();

        overFlowListener(publisher);

        profileListener(publisher);

        commentBtnListener();

        seeAllCommentsListener();

        full_post_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(0);
                anim.setDuration(300);

                full_post_bookmark.startAnimation(anim);

                if(flag) {
                    Drawable drawable = ContextCompat.getDrawable(FullPostActivity.this, R.drawable.ic_baseline_bookmark_24px);
                    full_post_bookmark.setImageDrawable(drawable);
                    if(drawable != null)
                    drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                    flag = false;
                } else {
                    Drawable drawable = ContextCompat.getDrawable(FullPostActivity.this, R.drawable.ic_outline_bookmark_border_24px);
                    full_post_bookmark.setImageDrawable(drawable);
                    if(drawable != null)
                    drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                    flag = true;
                }

            }
        });

        mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
                .collection("bookmarks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot : documents) {
                    Object bookMarkItemId = documentSnapshot.get("bookMarkItemId");
                    bookMarkItemIdd = (String) bookMarkItemId;
                }
                if(bookMarkItemIdd != null) {
                    if (bookMarkItemIdd.equals(postId)) {
                        full_post_bookmark.setImageDrawable(ContextCompat.getDrawable(FullPostActivity.this, R.drawable.ic_baseline_bookmark_24px));
                        flag = false;
                        gag = false;
                    }

                }

            }
        });


    }



    private void overFlowListener(User publisher) {
        overFlowBtn.setOnClickListener(new PostAnsCrackItemOverflowListener(this, publisher));
    }

    private void handleIntent() {
        Intent intent=getIntent();
        publisherId = intent.getStringExtra("PUBLISHER_ID");
        postId = intent.getStringExtra("POST_ID");
        postData = intent.getStringExtra(EXTRA_CONTENT);
        imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        imageThumb = intent.getStringExtra(EXTRA_THUMB);
        publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAME);
        posTime = intent.getStringExtra(EXTRA_TIME);
        publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DP);

        comments = intent.getLongExtra("COMMENTS", 0);
        views = intent.getLongExtra("VIEWS", 0);
        upVotes = intent.getLongExtra("UPVOTES", 0);
    }


    private void seeAllCommentsListener() {
        seeAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeAllComments.setEnabled(false);

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts").document(postId).
                        collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    private void setupCommentsRecycler() {
        mComments = new ArrayList<>();
        mAdapter = new CommentsAdapter(mComments);

        commentsRecycler = findViewById(R.id.commentsRecycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(FullPostActivity.this));
        commentsRecycler.setAdapter(mAdapter);
    }

    private void commentBtnListener() {
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FullPostActivity.this, CommentsActivity.class);
                intent1.putExtra("EXTRA_PUBLISHER_IDDD", publisherId);
                intent1.putExtra("EXTRA_FEED_ITEM_ID", postId);
                startActivity(intent1);
            }
        });

        commentsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FullPostActivity.this, CommentsActivity.class);
                intent1.putExtra("EXTRA_PUBLISHER_IDDD", publisherId);
                intent1.putExtra("EXTRA_FEED_ITEM_ID", postId);
                startActivity(intent1);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        seeAllComments.setEnabled(true);

if(!flag && gag) {
    Bookmark bookmark = new Bookmark();
    bookmark.setBookMarkerId(FirebaseUtil.getCurrentUserId());
    bookmark.setBookMarkItemId(postId);
    bookmark.setBookMarkType(BookMarkType.POST);
    bookmark.setPublisherId(publisherId);

    mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
            .collection("bookmarks").add(bookmark).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            mFirebaseUtil.mFirestore.collection("users").document(publisherId)
                    .collection("posts").document(postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                    noOfBookMarks = noOfBookMarks + 1;

                    Map<String, Object> map = new HashMap<>();
                    map.put("bookMarks", noOfBookMarks);

                    mFirebaseUtil.mFirestore.collection("users").document(publisherId)
                            .collection("posts").document(postId).update(map);

                }
            });
        }
    });
} else if(flag && !gag) {

    CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users")
            .document(FirebaseUtil.getCurrentUserId()).collection("bookmarks");
    Query query = collectionReference.whereEqualTo("bookMarkItemId", postId);

    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        @Override
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            DocumentSnapshot documentSnapshot = documents.get(0);
            String id = documentSnapshot.getId();

            mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
                    .collection("bookmarks").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mFirebaseUtil.mFirestore.collection("users").document(publisherId)
                            .collection("posts").document(postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                            noOfBookMarks = noOfBookMarks - 1;

                            Map<String, Object> map = new HashMap<>();
                            map.put("bookMarks", noOfBookMarks);

                            mFirebaseUtil.mFirestore.collection("users").document(publisherId)
                                    .collection("posts").document(postId).update(map);

                        }
                    });

                }
            });

        }
    });

}
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

    public void bindViewsUpvotes() {
        try {
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

            views = views+1;
            Map<String, Object> map= new HashMap();
            map.put("views",views);

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
        full_post_bookmark = findViewById(R.id.full_post_bookmark);
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
        if(comments==0)
            seeAllComments.setVisibility(View.INVISIBLE);
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

    public static Intent newIntent(Context context, String content, String publishedImg, String imageThumb,
                                   String timeAgo, User publisher, String postId, long comments, long views, long upvotes) {

        Intent intent = new Intent(context, FullPostActivity.class);
        intent.putExtra(EXTRA_CONTENT,content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        intent.putExtra("POST_ID", postId);
        intent.putExtra("PUBLISHER_ID",publisher.getUserId());
        intent.putExtra("COMMENTS", comments);
        intent.putExtra("VIEWS", views);
        intent.putExtra("UPVOTES", upvotes);
        if(publishedImg!=null) {
            intent.putExtra(EXTRA_IMAGE, publishedImg);
            intent.putExtra(EXTRA_THUMB, imageThumb);
        }
        return intent;
    }

}
