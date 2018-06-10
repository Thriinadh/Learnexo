package com.learnexo.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.model.user.Follow;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class FollowingListActivity extends AppCompatActivity {

    Toolbar setupToolbar;
    private boolean isFrom;
    private boolean isFromOthers;
    private String ingORer;
    private String userId;
    private String publisherId;

    private List<Follow> mFollowList;
    private FollowListAdapter mAdapter;

    private FirebaseUtil mFireBaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        setupToolbar();
        setupRecyclerView();

        Intent intent = getIntent();
        isFrom = intent.getBooleanExtra("EXTRA_IS_FROM_FOLLOWING", false);
        isFromOthers =intent.getBooleanExtra("EXTRA_IS_FROM_OTHERS_FOLLOWING", false);
        publisherId = intent.getStringExtra("EXTRA_PUBLISHER_ID_OTHERS");

        if (isFrom)
            ingORer = "following";
        else
            ingORer = "followers";

        if (isFromOthers)
            userId = publisherId;
        else
            userId = FirebaseUtil.getCurrentUserId();


        mFireBaseUtil.mFirestore.collection("users").document(userId)
                .collection(ingORer).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot:documents){
                    mFollowList.add(documentSnapshot.toObject(Follow.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setupToolbar() {
        setupToolbar = findViewById(R.id.include);
        setSupportActionBar(setupToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (isFrom)
            getSupportActionBar().setTitle("Following");
            else
                getSupportActionBar().setTitle("Followers");
        }
    }

    private void setupRecyclerView() {
        mFollowList = new ArrayList<>();
        mAdapter = new FollowListAdapter(mFollowList);
        RecyclerView followRecyclerView = findViewById(R.id.followRecyclerView);
        followRecyclerView.setLayoutManager(new LinearLayoutManager(FollowingListActivity.this));
        followRecyclerView.setAdapter(mAdapter);
    }

}
