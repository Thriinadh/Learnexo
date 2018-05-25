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


public class UserQuestionsFragment extends Fragment {

    private List<Question> mQuestions;
    private UserQuesRecyclerAdapter mAdapter;
    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;
    private boolean isOtherProfile;

    public UserQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_questions, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null) {
            isOtherProfile = (boolean) arguments.get("IS_OTHER_PROFILE");
            otherProfileId = (String) arguments.get("OTHER_PROFILE_ID");
            otherProfileName = (String) arguments.get("OTHER_PROFILE_NAME");
            otherProfileDP = (String) arguments.get("OTHER_PROFILE_DP");
        }

        setupRecyclerView(view);

        getCracksFromDatabase();

        return view;
    }

    private void getCracksFromDatabase() {

        String userId;
        if(isOtherProfile)
            userId=otherProfileId;
        else
            userId=FirebaseUtil.getCurrentUserId();
        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users").document(userId).collection("questions");
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
//        mQuestions = new ArrayList<>();
//        mAdapter = new UserQuesRecyclerAdapter(mQuestions);

        mQuestions = new ArrayList<>();
        if(isOtherProfile)
            mAdapter = new UserQuesRecyclerAdapter(mQuestions, true, otherProfileId, otherProfileName, otherProfileDP);
        else
            mAdapter = new UserQuesRecyclerAdapter(mQuestions, false, null, null, null);


        RecyclerView profilePostsRecyclerView = view.findViewById(R.id.profilePostsRecycler);
        profilePostsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profilePostsRecyclerView.setAdapter(mAdapter);
    }

}
