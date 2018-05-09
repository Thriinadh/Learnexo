package com.learnexo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.learnexo.main.MainActivity;
import com.learnexo.main.R;
import com.learnexo.main.TabsActivity;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {

  //  public static final String EXTRA_PUBLISH_TYPE ="com.learnexo.fragment.FeedFragment.EXTRA_PUBLISH_TYPE";

    private CircleImageView mCircleImageView;
    private TextView mNameTView;

    private List<FeedItem> mFeedItems;
    private FeedRecyclerAdapter mAdapter;

    private TextView askQuestionTView;

    private String mUserId;


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
        askQuestionListener();

        getDPandUserNameandSet();
        generateFeedItemList();

        return view;
    }

    private void askQuestionListener() {
        askQuestionTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabsActivity tabsActivity=(TabsActivity)getActivity();
                if(tabsActivity!=null)
                tabsActivity.startPublishActivity(getString(R.string.askYourQuestion));
            }
        });
    }

    private void generateFeedItemList() {

        //fetching posts, answers questions, challenges, news separately and mix them up in feed items list
        //check if feed items list size is <=20, if yes refill the feed list
        //refill the feed item list for every 10 min


        //get Posts, Answers tagged in interests, based on related topics
        //posted by related people
        //involved by related people in Like division
        //with more likes, comments, shares, bookmarks and views (Like division)
        //related to history, timing

        //get Unanswered Questions/Challenges tagged in interests
        //asked by related people
        //based on related topics
        //requested to the logged in user
        //based on history, timing

        //related people =
        // (PriorityProfiles, Following, FollowingOfFollowing, nearby, visited profiles, topProfiles...)
        //Interests, more likes, shares, comments, bookmarks
        //Based on his history, check his timing of usage
        //related topics to follow
        //related people to follow
        //related questions, posts and challenges
        //his followers posts and friends posts
        //news and girls
        //priority profiles


        List<InterestFeed> interestFeeds = getInterestFeeds();
        Log.d("interest feeds", interestFeeds.toString());

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

    private List<InterestFeed> getInterestFeeds() {
        List<InterestFeed> interestFeeds=new ArrayList<>();
        InterestFeed interestFeed;
        Map<String, Object> intestMap = mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("interests").
                document("doc1").get().getResult().getData();
        Set<String> interests = intestMap.keySet();
        for (String interest:interests){
            List<DocumentSnapshot> interest_feed_docs = mFirebaseUtil.mFirestore.collection("interest_feed").get().getResult().getDocuments();
            for (DocumentSnapshot documentSnap:interest_feed_docs) {
                Map<String, Object> interestFeedMap = documentSnap.getData();

                interestFeed=new InterestFeed();
                String interest1 = (String) interestFeedMap.get("interest");
                if(interest.equals(interest1)){
                    interestFeed.setInterest(interest1);
                    interestFeed.setFeedItemId((String) interestFeedMap.get("feedItemId"));
                    interestFeed.setPublisherId((String) interestFeedMap.get("publisherId"));

                    interestFeeds.add(interestFeed);
                }
            }
        }

        return interestFeeds;
    }

    private void getDPandUserNameandSet() {
        mUserId = FirebaseUtil.getCurrentUserId();
        mFirebaseUtil.mFirestore.collection("users").document(mUserId).
                collection("reg_details").document("doc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()) {
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");
                        mNameTView.setText(firstName.concat(" "+lastName));

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_photo);

                        String image = (null==MainActivity.photoUrl)? snapshot
                                .getString("dpUrl"):MainActivity.photoUrl;

                        if (image!=null&&null!=getActivity()) {
                            Glide.with(getActivity().getApplicationContext()).load(image).apply(placeholderRequest).into(mCircleImageView);
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

    }

    private void setupFeedRecyclerAdapter(View view) {
        mFeedItems = new ArrayList<>();
        mAdapter = new FeedRecyclerAdapter(mFeedItems);

        RecyclerView feedRecyclerView = view.findViewById(R.id.feed_list_recycler_view);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedRecyclerView.setAdapter(mAdapter);

        feedRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                TabsActivity tabsActivity=(TabsActivity) getActivity();

                tabsActivity.hideCardView();
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }

}
