package com.learnexo.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.main.FeedRecyclerAdapter;
import com.learnexo.main.MainActivity;
import com.learnexo.main.PublishActivity;
import com.learnexo.main.R;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {

    public static final String EXTRA_PUBLISH_TYPE ="com.learnexo.fragment.FeedFragment.EXTRA_PUBLISH_TYPE";

    private CircleImageView mCircleImageView;
    private TextView mNameTView;

    private List<FeedItem> mFeedItems;
    private FeedRecyclerAdapter mAdapter;

    private Button mShareInfoBtn;
    private Button mAskBtn;
    private Button mChallengeBtn;
    private FloatingActionButton mFloatingBtn;
    private CardView mCardView;
    private TextView askQuestionTView;

    private String mUserId;
    boolean flag = true;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        setupFeedRecyclerAdapter(view);
        wireViews(view);

        getDPandUserNameandSet();
        generateFeedItemList();

        askQuestionTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.askYourQuestion));
            }
        });

        floatingBtnListener();
        shareInfoBtnListener();
        askQuestionBtnListener();
        postChallengeBtnListener();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCardView.setVisibility(View.INVISIBLE);
        flag = true;
    }

    private void startPublishActivity(String publish_type) {
        Intent shareIntent = new Intent(getActivity(), PublishActivity.class);
        shareIntent.putExtra(EXTRA_PUBLISH_TYPE, publish_type);
        startActivity(shareIntent);
    }

    private void shareInfoBtnListener() {
        mShareInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  startPublishActivity(getString(R.string.share_info));
                }
            });
    }

    private void postChallengeBtnListener() {
        mChallengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.postChallenge));
            }
        });
    }

    private void askQuestionBtnListener() {
        mAskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity(getString(R.string.askYourQuestion));
            }
        });
    }

    private void floatingBtnListener() {
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag) {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                       // int cx = mCardView.getWidth() / 2;
                       // int cy = mCardView.getHeight() / 2;

                        // get the center for the clipping circle
                        int cx = (view.getLeft() + view.getRight()) / 2;
                        int cy = (view.getTop() + view.getBottom()) / 2;

                        int startRadius = 0;
                       // get the final radius for the clipping circle
                        int endRadius = Math.max(view.getWidth(), view.getHeight());

                        // get the final radius for the clipping circle
                       // float finalRadius = (float) Math.hypot(cx, cy);

                        // create the animator for this view (the start radius is zero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(mCardView, cx, cy, startRadius, endRadius);

                        // make the view visible and start the animation
                        mCardView.setVisibility(View.VISIBLE);
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        mCardView.setVisibility(View.VISIBLE);
                    }

                    flag = false;
                } else {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                       // int cx = mCardView.getWidth() / 2;
                       // int cy = mCardView.getHeight() / 2;

                        // get the center for the clipping circle
                        int cx = (view.getLeft() + view.getRight()) / 2;
                        int cy = (view.getTop() + view.getBottom()) / 2;

                        // get the initial radius for the clipping circle
                       // float initialRadius = (float) Math.hypot(cx, cy);

                        int endRadius = 0;
                        // get the final radius for the clipping circle
                        int startRadius = Math.max(view.getWidth(), view.getHeight());

                         // create the animation (the final radius is zero)
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(mCardView, cx, cy, startRadius, endRadius);

                       // make the view invisible when the animation is done
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mCardView.setVisibility(View.INVISIBLE);
                            }
                        });

                     // start the animation
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        mCardView.setVisibility(View.INVISIBLE);
                    }
                    flag = true;
                }

            }
        });
    }

    private void generateFeedItemList() {
        mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null)
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        Post post;
                        switch (doc.getType()) {

                            case ADDED:
                                post = doc.getDocument().toObject(Post.class);
                                mFeedItems.add(post);
                                mAdapter.notifyDataSetChanged();
                                break;

                            case REMOVED:
                                post = doc.getDocument().toObject(Post.class);
                                mFeedItems.remove(post);
                                mAdapter.notifyDataSetChanged();
                                break;

                            case MODIFIED:
                                post = doc.getDocument().toObject(Post.class);
                                mFeedItems.remove(post);
                                mAdapter.notifyDataSetChanged();
                                break;
                        }

                    }

            }
        });
    }

    private void getDPandUserNameandSet() {
        mUserId = FirebaseUtil.getCurrentUserId();
        mFirebaseUtil.mFirestore.collection("Users").document(mUserId).
                collection("Setup Details").document("Setup Fields").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {

                        String name = task.getResult().getString("Nick name");
                        mNameTView.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_photo);

                        String image = (null==MainActivity.photoUrl)?task.getResult()
                                .getString("Image"):MainActivity.photoUrl;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && image!=null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(image)
                                    .apply(placeholderRequest).into(mCircleImageView);
                        }
                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void wireViews(View view) {
        mCircleImageView = view.findViewById(R.id.userCircleIView);
        mNameTView = view.findViewById(R.id.userNameTView);

        askQuestionTView = view.findViewById(R.id.askQuestionTView);
        // ProgressBar progressBar = view.findViewById(R.id.progressBar);

        mFloatingBtn = view.findViewById(R.id.floatBtn);
        mCardView = view.findViewById(R.id.cardView);

        mShareInfoBtn = view.findViewById(R.id.shareBtn);
        mAskBtn = view.findViewById(R.id.askBtn);
        mChallengeBtn = view.findViewById(R.id.challengeBtn);
    }

    private void setupFeedRecyclerAdapter(View view) {
        mFeedItems = new ArrayList<>();
        mAdapter = new FeedRecyclerAdapter(mFeedItems);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feed_list_recycler_view);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedRecyclerView.setAdapter(mAdapter);
    }

    public void hideCardView() {
        if(mCardView != null) {
            mCardView.setVisibility(View.INVISIBLE);
            flag = true;
        }
    }
}
