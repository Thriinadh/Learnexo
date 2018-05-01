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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.main.FeedRecyclerAdapter;
import com.learnexo.main.MainActivity;
import com.learnexo.main.R;
import com.learnexo.main.PublishActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.model.feed.post.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {

    private CircleImageView userCircleIView;
    private TextView userNameTView;
    private TextView askQuestionTView;

    private RecyclerView feedListRecyclerView;
    private List<Post> feed_items_list;
    private FeedRecyclerAdapter feedRecyclerAdapter;

    private ProgressBar progressBar;

    private Button shareInfoBtn;
    private Button askBtn;
    private Button challengeBtn;

    private String user_id;

    private FloatingActionButton floatingBtn;
    private CardView cardView;
    boolean flag = true;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firebaseFirestore;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        setupRecycler(view);

        wiringViews(view);

        getDPandUserNameandSet();

        getFreshPostsAndPopulatePostList();

        handleFloatingBtn();

        handleShareInfoBtn();

        handleQuestionBtn();

        handleChallengeBtn();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        cardView.setVisibility(View.INVISIBLE);
        flag = true;
    }

    private void handleShareInfoBtn() {
        shareInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent shareIntent = new Intent(getActivity(), PublishActivity.class);
//                    shareIntent.putExtra("PUBLISH_TYPE", );
//                    startActivity(shareIntent);

                    startPublishActivity("Share Info");

                }
            });
    }

    private void startPublishActivity(String publish_type) {

        Intent shareIntent = new Intent(getActivity(), PublishActivity.class);
                    shareIntent.putExtra("PUBLISH_TYPE", publish_type);
                    startActivity(shareIntent);

    }

    private void handleChallengeBtn() {
        challengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity("Post challenge");
            }
        });
    }

    private void handleQuestionBtn() {
        askBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPublishActivity("Ask question");
            }
        });
    }

    private void handleFloatingBtn() {
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag) {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                       // int cx = cardView.getWidth() / 2;
                       // int cy = cardView.getHeight() / 2;

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
                                ViewAnimationUtils.createCircularReveal(cardView, cx, cy, startRadius, endRadius);

                        // make the view visible and start the animation
                        cardView.setVisibility(View.VISIBLE);
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        cardView.setVisibility(View.VISIBLE);
                    }

                    flag = false;
                } else {

                    // Check if the runtime version is at least Lollipop
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // get the center for the clipping circle
                       // int cx = cardView.getWidth() / 2;
                       // int cy = cardView.getHeight() / 2;

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
                                ViewAnimationUtils.createCircularReveal(cardView, cx, cy, startRadius, endRadius);

                       // make the view invisible when the animation is done
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                cardView.setVisibility(View.INVISIBLE);
                            }
                        });

                     // start the animation
                        anim.start();
                    } else {
                        // set the view to visible without a circular reveal animation below Lollipop
                        cardView.setVisibility(View.INVISIBLE);
                    }
                    flag = true;
                }

            }
        });
    }

    private void getFreshPostsAndPopulatePostList() {
        firebaseFirestore.collection("users").document(user_id).collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null)
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

//                        if (doc.getType() == DocumentChange.Type.ADDED) {
//
//                            Post Post = doc.getDocument().toObject(Post.class);
//                            feed_items_list.add(Post);
//                            feedRecyclerAdapter.notifyDataSetChanged();
//
//                        }

                        Post post;
                        switch (doc.getType()) {

                            case ADDED:
                                post = doc.getDocument().toObject(Post.class);
                                feed_items_list.add(post);
                                feedRecyclerAdapter.notifyDataSetChanged();
                                break;

                            case REMOVED:
                                post = doc.getDocument().toObject(Post.class);
                                feed_items_list.remove(post);
                                feedRecyclerAdapter.notifyDataSetChanged();
                                break;

                            case MODIFIED:
                                post = doc.getDocument().toObject(Post.class);
                                feed_items_list.remove(post);
                                feedRecyclerAdapter.notifyDataSetChanged();
                                break;
                        }

                    }

            }
        });
    }

    private void getDPandUserNameandSet() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
            user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(user_id).
                collection("Setup Details").document("Setup Fields").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    if(task.getResult().exists()) {

                        String name = task.getResult().getString("Nick name");

                        String image = (null==MainActivity.photoUrl)?task.getResult()
                                .getString("Image"):MainActivity.photoUrl;

                        userNameTView.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_photo);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && image!=null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(image)
                                    .apply(placeholderRequest).into(userCircleIView);
                        }

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void wiringViews(View view) {
        userCircleIView = view.findViewById(R.id.userCircleIView);
        userNameTView = view.findViewById(R.id.userNameTView);
        askQuestionTView = view.findViewById(R.id.askQuestionTView);

        progressBar = view.findViewById(R.id.progressBar);

        floatingBtn = view.findViewById(R.id.floatBtn);
        cardView = view.findViewById(R.id.cardView);

        shareInfoBtn = view.findViewById(R.id.shareBtn);
        askBtn = view.findViewById(R.id.askBtn);
        challengeBtn = view.findViewById(R.id.challengeBtn);
    }

    private void setupRecycler(View view) {
        feed_items_list = new ArrayList<>();
        feedListRecyclerView = view.findViewById(R.id.feed_list_recycler_view);
        feedRecyclerAdapter = new FeedRecyclerAdapter(feed_items_list);

        feedListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedListRecyclerView.setAdapter(feedRecyclerAdapter);
    }

    public void hideCardview() {
        if(cardView != null) {
            cardView.setVisibility(View.INVISIBLE);
            flag = true;
        }
    }
}
