package com.learnexo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.main.UserPostsRecyclerAdapter;
import com.learnexo.main.R;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;


public class UserPostsFragment extends Fragment {

    private List<Post> mPosts;
    private UserPostsRecyclerAdapter mAdapter;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();
    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;
    private boolean isOtherProfile;

    public UserPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_posts, container, false);
        Bundle arguments = getArguments();
        if(arguments!=null) {
            isOtherProfile = (boolean) arguments.get("IS_OTHER_PROFILE");
            otherProfileId = (String) arguments.get("OTHER_PROFILE_ID");
            otherProfileName = (String) arguments.get("OTHER_PROFILE_NAME");
            otherProfileDP = (String) arguments.get("OTHER_PROFILE_DP");
        }
        setupRecyclerView(view);

        getPostsFromDatabase();

        // Inflate the layout for this fragment
        return view;
    }

    private void getPostsFromDatabase() {
        String userId;
        if(isOtherProfile)
            userId=otherProfileId;
        else
            userId=FirebaseUtil.getCurrentUserId();
        mFirebaseUtil.mFirestore.collection("users").document(userId)
                .collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot:documents){
                    mPosts.add(documentSnapshot.toObject(Post.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        mPosts = new ArrayList<>();
        if(isOtherProfile)
        mAdapter = new UserPostsRecyclerAdapter(mPosts, true, otherProfileId, otherProfileName, otherProfileDP);
        else
            mAdapter = new UserPostsRecyclerAdapter(mPosts, false, null, null, null);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
