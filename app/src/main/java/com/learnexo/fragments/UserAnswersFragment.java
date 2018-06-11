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

public class UserAnswersFragment extends Fragment {

    private List<Answer> mAnswers;
    private UserAnswersRecyclerAdapter mAdapter;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;
    private boolean isOtherProfile;

    public UserAnswersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_answers, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null) {
            isOtherProfile = (boolean) arguments.get("IS_OTHER_PROFILE");
            otherProfileId = (String) arguments.get("OTHER_PROFILE_ID");
            otherProfileName = (String) arguments.get("OTHER_PROFILE_NAME");
            otherProfileDP = (String) arguments.get("OTHER_PROFILE_DP");
        }

        setupRecyclerView(view);

        getAnswersFromDatabase();

        return view;
    }

    private void getAnswersFromDatabase() {
        String userId;
        if(isOtherProfile)
            userId=otherProfileId;
        else
            userId=FirebaseUtil.getCurrentUserId();

        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users").document(userId).collection("answers");
        Query query = collectionReference.whereEqualTo("type", 1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot documentSnapshot:documents){
                    Answer answer = documentSnapshot.toObject(Answer.class);
                    if(answer!=null) {
                        answer.setFeedItemId(documentSnapshot.getId());
                        mAnswers.add(answer);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupRecyclerView(View view) {
        mAnswers = new ArrayList<>();
        if(isOtherProfile)
            mAdapter = new UserAnswersRecyclerAdapter(mAnswers, true, otherProfileId, otherProfileName, otherProfileDP, false);
        else
            mAdapter = new UserAnswersRecyclerAdapter(mAnswers, false, null, null, null, false);

        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
