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

    public UserPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_posts, container, false);

        setupRecyclerView(view);

        getPostsFromDatabase();

        // Inflate the layout for this fragment
        return view;
    }

    private void getPostsFromDatabase() {
        mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
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
        mAdapter = new UserPostsRecyclerAdapter(mPosts);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
