package com.learnexo.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.main.R;
import com.learnexo.main.UserQuesRecyclerAdapter;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;


public class ProfileQuestions extends Fragment {

    private List<Question> mQuestions;
    private UserQuesRecyclerAdapter mAdapter;
    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public ProfileQuestions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_questions, container, false);

        setupRecyclerView(view);

        getCracksFromDatabase();

        return view;
    }

    private void getCracksFromDatabase() {
        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("questions");
        Query query = collectionReference.whereEqualTo("challenge", false);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot documentSnapshot:documents){
                    mQuestions.add(documentSnapshot.toObject(Question.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        mQuestions = new ArrayList<>();
        mAdapter = new UserQuesRecyclerAdapter(mQuestions);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
