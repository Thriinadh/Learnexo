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
import com.learnexo.main.UserAnswersRecyclerAdapter;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ProfileAnswers extends Fragment {

    private List<Answer> mAnswers;
    private UserAnswersRecyclerAdapter mAdapter;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public ProfileAnswers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_answers, container, false);

        setupRecyclerView(view);

        getAnswersFromDatabase();

        return view;
    }

    private void getAnswersFromDatabase() {
        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("answers");
        Query query = collectionReference.whereEqualTo("type", 1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot documentSnapshot:documents){
                    mAnswers.add(documentSnapshot.toObject(Answer.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        mAnswers = new ArrayList<>();
        mAdapter = new UserAnswersRecyclerAdapter(mAnswers);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
