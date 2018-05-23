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
import com.learnexo.main.UserChallengesRecyclerAdapter;
import com.learnexo.main.UserQuesRecyclerAdapter;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ProfileChallengeFragment extends Fragment {

    private List<Question> mChallenges;
    private UserChallengesRecyclerAdapter mAdapter;
    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public ProfileChallengeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_challenge, container, false);

        setupRecyclerView(view);

        getCracksFromDatabase();

        return view;
    }

    private void getCracksFromDatabase() {
        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId()).collection("questions");
        final Query query = collectionReference.whereEqualTo("challenge", true);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                int totalChallenges = documents.size();

                for(DocumentSnapshot documentSnapshot:documents){
                    mChallenges.add(documentSnapshot.toObject(Question.class));

                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        mChallenges = new ArrayList<>();
        mAdapter = new UserChallengesRecyclerAdapter(mChallenges);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
