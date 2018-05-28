package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.post.PostDetails;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;
import com.learnexo.util.MyBounceInterpolator;

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

    private ImageView fullPostCommentBtn;
    private ImageView fullPostLikeBtn;
    private ImageView overFlowBtn;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        wireViews();
        setupToolbar();

        Intent intent=getIntent();
        publisherId=intent.getStringExtra("PUBLISHER_ID");
        postId = intent.getStringExtra("POST_ID");
        postData = intent.getStringExtra(EXTRA_CONTENT);
        String imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        String imageThumb = intent.getStringExtra(EXTRA_THUMB);
        String publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAME);
        String posTime = intent.getStringExtra(EXTRA_TIME);
        String publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DP);

        final User publisher = new User(publisherId, publisherName, publisherDP);


        bindData(imagePosted, imageThumb, publisherName, posTime, publisherDP);
        new GetPostViewsAndUpVotes().execute(publisherId, postId);

        overFlowBtn.setOnClickListener(new PostAnsCrackItemOverflowListener(this, publisher));

        profileListener(publisher);

        fullPostCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  onShowPopup(view);
            }
        });
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FullPostActivity.this, CommentsActivity.class);
                intent1.putExtra("EXTRA_PUBLISHER_IDDD", publisherId);
                intent1.putExtra("EXTRA_POST_ITEM_ID", postId);
                startActivity(intent1);
              //  onShowPopup(view);
            }
        });

    }

//
//    // call this method when required to show popup
//    public void onShowPopup(View v){
//
//        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        // inflate the custom popup layout
//        View inflatedView = layoutInflater.inflate(R.layout.activity_comments, null,false);
//        // find the ListView in the popup layout
////        ListView listView = (ListView)inflatedView.findViewById(R.id.commentsListView);
////        LinearLayout headerView = (LinearLayout)inflatedView.findViewById(R.id.headerLayout);
//        // get device size
//        Display display = getWindowManager().getDefaultDisplay();
//        final Point size = new Point();
//        display.getSize(size);
////        mDeviceHeight = size.y;
//        DisplayMetrics displayMetrics = FullPostActivity.this.getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//
//        // fill the data to the list items
////        setSimpleList(listView);
//
//
//        // set height depends on the device size
//        PopupWindow popWindow = new PopupWindow(inflatedView, width,height, true );
//        // set a background drawable with rounders corners
//        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
//
//        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popWindow.setHeight(height-70);
//
//        popWindow.setAnimationStyle(R.style.PopupAnimation);
//
//        // show the popup at bottom of the screen and set some margin at bottom ie,
//        popWindow.showAtLocation(v, Gravity.BOTTOM, 0,0);
//    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
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

                    new LikeBtnListener(fullPostLikeBtn,likesCount,flag, publisherId,postId, upVotes,FullPostActivity.this, false)
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
        fullPostCommentBtn = findViewById(R.id.full_post_comment);
        overFlowBtn = findViewById(R.id.imageView);
        fullText = findViewById(R.id.full_text);
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.userNameTView);
        timeOfPost = findViewById(R.id.feed_time);
        postedImage = findViewById(R.id.postedImage);
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


    public class GetPostViewsAndUpVotes extends AsyncTask<Object, Object,PostDetails> {

        @Override
        protected PostDetails doInBackground(Object[] objects) {
            Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").
                    document((String) objects[0]).collection("posts").document((String) objects[1]).get();
            PostDetails postDetails=null;

            try {
                DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);

                postDetails = new PostDetails();
                postDetails.setNoOfLikes((Long) documentSnapshot.get("upVotes"));
                postDetails.setNoOfViews((Long) documentSnapshot.get("views"));

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return postDetails;
        }

        @Override
        protected void onPostExecute(PostDetails result) {
            bindViewsUpvotes(result);
        }

    }
}
