package com.learnexo.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.main.R;
import com.learnexo.main.TabsActivity;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedFragment extends Fragment {

    private CircleImageView mCircleImageView;
    private TextView mNameTView;

    private List<FeedItem> mFeedItems;
    private FeedRecyclerAdapter mAdapter;

    private TextView askQuestionTView;

    private String mUserId;
    public static String sDpUrl;
    public static String sName;

    ConnectivityManager cm;


    FirebaseUtil mFireBaseUtil = new FirebaseUtil();


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
        getDPandUserName();
        setupFeedRecyclerAdapter(view);
        wireViews(view);

        askQuestionListener();
        if (internet_connection()) {
            generateFeedItemList();
        } else {
            Toast.makeText(getActivity(), "Cannot retrieve data coz client is offline", Toast.LENGTH_SHORT).show();
        }

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


    //    POST,ANSWER,CRACK,QUESTION,CHALLENGE,
    //    UP_VOTE,DOWN_VOTE,COMMENT,SHARE,BOOKMARK,
    //    FOLLOW,TAG,SEARCH,
    //
    //    EDIT, COMMENT_ON_QUES,
    //
    //    SUBSCRIBE,COMPLETE_SUB,COMPLETE_TEST,
    //    ADD_IVQ,ADD_TEST_Q,
    //    REQUEST_HELP,GUIDE,
    //    PROFILE_UPDATE,INTEREST_UPDATE

    //    interests, related topics, trending, search history, activity history,
    //    topProfiles, nearby, girls, priority profiles, visited profiles
    //    show related topics to follow
    //    show related people to follow
    //    news

    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        if (getActivity() != null)
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    private void generateFeedItemList() {

        mFireBaseUtil.mFirestore.collection("users").document(mUserId).collection("interests").
                document("doc1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            Map<String, Object> intestMap =null;
            List<InterestFeed> interestFeeds=new ArrayList<>();
            InterestFeed interestFeed;
            int i=0;
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                intestMap=task.getResult().getData();

                Set<String> interests = null;
                if (intestMap != null) {
                    interests = intestMap.keySet();
                }

                if (interests != null) {
                    for (final String userInterest:interests){

                        mFireBaseUtil.mFirestore.collection("interest_feed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            List<DocumentSnapshot> interest_feed_docs;
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                interest_feed_docs=task.getResult().getDocuments();

                                for (DocumentSnapshot documentSnap:interest_feed_docs) {
                                    Map<String, Object> interestFeedData = documentSnap.getData();

                                    String postInterest = (String) interestFeedData.get("interest");
                                    if(userInterest.equals(postInterest)){
                                        interestFeed=new InterestFeed();
                                        interestFeed.setFeedItemId((String) interestFeedData.get("feedItemId"));
                                        interestFeed.setPublisherId((String) interestFeedData.get("publisherId"));

                                        interestFeed.setFeedType(((Long)interestFeedData.get("feedType")).intValue());

                                        interestFeeds.add(interestFeed);
                                    }
                                }

                                if(i!=1)

                                for (final InterestFeed interestFeed: interestFeeds) {
                                    i=1;

                                    switch (interestFeed.getFeedType()){
                                        case FeedItem.POST:
                                        mFireBaseUtil.mFirestore.collection("users").document(interestFeed.getPublisherId())
                                                .collection("posts").document(interestFeed.getFeedItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                Post post=documentSnapshot.toObject(Post.class);
                                                if(post!=null) {
                                                    post.setFeedItemId(documentSnapshot.getId());
                                                    mFeedItems.add(post);
                                                }
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        break;

                                        case FeedItem.ANSWER:

                                            mFireBaseUtil.mFirestore.collection("users").document(interestFeed.getPublisherId())
                                                    .collection("answers").document(interestFeed.getFeedItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();

                                                    Answer answer=documentSnapshot.toObject(Answer.class);
                                                    if(answer!=null) {
                                                        answer.setFeedItemId(documentSnapshot.getId());
                                                        mFeedItems.add(answer);
                                                    }
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                            break;


                                        case FeedItem.CRACK:

                                            mFireBaseUtil.mFirestore.collection("users").document(interestFeed.getPublisherId())
                                                    .collection("answers").document(interestFeed.getFeedItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();

                                                    Answer crack=documentSnapshot.toObject(Answer.class);
                                                    if(crack!=null) {
                                                        crack.setFeedItemId(documentSnapshot.getId());
                                                        mFeedItems.add(crack);
                                                    }
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                            break;

                                        case FeedItem.QUESTION:

                                            mFireBaseUtil.mFirestore.collection("users").document(interestFeed.getPublisherId())
                                                    .collection("questions").document(interestFeed.getFeedItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();

                                                    Question noAnsQuestion=documentSnapshot.toObject(Question.class);
                                                    if(noAnsQuestion!=null) {
                                                        noAnsQuestion.setFeedItemId(documentSnapshot.getId());
                                                        mFeedItems.add(noAnsQuestion);
                                                    }
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                            break;

                                        case FeedItem.CHALLENGE:

                                            mFireBaseUtil.mFirestore.collection("users").document(interestFeed.getPublisherId())
                                                    .collection("questions").document(interestFeed.getFeedItemId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();

                                                    Question noCrackChallenge=documentSnapshot.toObject(Question.class);
                                                    if(noCrackChallenge!=null) {
                                                        noCrackChallenge.setFeedItemId(documentSnapshot.getId());
                                                        mFeedItems.add(noCrackChallenge);
                                                    }
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                            break;
                                    }

                                }

                            }

                        });
                    }
                }

            }

        });

    }


    private void getDPandUserName() {
        mUserId = FirebaseUtil.getCurrentUserId();
        mFireBaseUtil.mFirestore.collection("users").document(mUserId).
                collection("reg_details").document("doc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()) {
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");

                        sName =firstName.concat(" "+lastName);
                        mNameTView.setText(sName);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.empty_profilee);

                        sDpUrl =  snapshot.getString("dpUrl");

                        if (sDpUrl !=null&&null!=getActivity()) {
                            Glide.with(getActivity().getApplicationContext()).load(sDpUrl).apply(placeholderRequest).into(mCircleImageView);
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