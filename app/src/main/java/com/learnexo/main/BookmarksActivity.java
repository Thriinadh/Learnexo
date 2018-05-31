package com.learnexo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.FeedRecyclerAdapter;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.likediv.Bookmark;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class BookmarksActivity extends AppCompatActivity {
    private List<FeedItem> mFeedItems;
    private FeedRecyclerAdapter mAdapter;

    FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bookmarks);

        setupFeedRecyclerAdapter();

    }
    private void setupFeedRecyclerAdapter() {
        mFeedItems = new ArrayList<>();
        mAdapter = new FeedRecyclerAdapter(mFeedItems);

        RecyclerView feedRecyclerView = findViewById(R.id.bookmarksRecycler);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(BookmarksActivity.this));
        feedRecyclerView.setAdapter(mAdapter);

        fetchBookMarks(FirebaseUtil.getCurrentUserId());


    }

        private void fetchBookMarks(String currentUserId) {

            mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("bookmarks").
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){

                        Bookmark bookmark = queryDocumentSnapshot.toObject(Bookmark.class);
                        String bookMarkItemId = bookmark.getBookMarkItemId();
                        String publisherId = bookmark.getPublisherId();

                                if (bookmark.getBookMarkType().name().equals("POST")) {

                                    mFirebaseUtil.mFirestore.collection("users").document(publisherId).
                                            collection("posts").document(bookMarkItemId).get().addOnSuccessListener(
                                            new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Post post = documentSnapshot.toObject(Post.class);
                                                    mFeedItems.add(post);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                                else {
                                    mFirebaseUtil.mFirestore.collection("users").document(publisherId).
                                            collection("answers").document(bookMarkItemId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Answer answer = documentSnapshot.toObject(Answer.class);
                                            mFeedItems.add(answer);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }




                    }

                }
            });

    }



}
