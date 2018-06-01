package com.learnexo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.learnexo.fragments.FeedRecyclerAdapter;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.likediv.Bookmark;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.video.Branch;
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

            //just for data insertion, it is not pat of bookmarks.
            insertBranchData();

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

    private void insertBranchData() {

        Gson gson=new Gson();


        Branch branch = gson.fromJson(

                "{\"mBranchName\":\"Fundamentals\",\"mStringSubjectMap\":{\"OS\":{\"mSubjectName\":\"OS\",\"isChecked\":false,\"mPrice" +
                "\":123.6,\"mStringChapterMap\":{\"chapter1\":{\"mChapterName\":\"chapter1\",\"mStringVideoLessonMap\":{\"video lesson 3\":{\"mVideoName\"" +
                ":\"video lesson 1\",\"uri\":\"aaaaaaaaa\"},\"video lesson 2\":{\"mVideoName\":\"video lesson 2\",\"uri\":\"bbbbbbbbbbb\"},\"video lesson " +
                "1\":{\"mVideoName\":\"video lesson 3\",\"uri\":\"cccccccccc\"}}},\"chapter2\":{\"mChapterName\":\"chapter2\",\"mStringVideoLessonMap\":{\"" +
                "video lesson 3\":{\"mVideoName\":\"video lesson 1\",\"uri\":\"aaaaaaaaa\"},\"video lesson 2\":{\"mVideoName\":\"video lesson 2\",\"uri\":\"" +
                "bbbbbbbbbbb\"},\"video lesson 1\":{\"mVideoName\":\"video lesson 3\",\"uri\":\"cccccccccc\"}}}}},\"CO\":{\"mSubjectName\":\"CO\",\"isChec" +
                "ked\":false,\"mPrice\":123.6,\"mStringChapterMap\":{\"chapter1\":{\"mChapterName\":\"chapter1\",\"mStringVideoLessonMap\":{\"video les" +
                "son 3\":{\"mVideoName\":\"video lesson 1\",\"uri\":\"aaaaaaaaa\"},\"video lesson 2\":{\"mVideoName\":\"video lesson 2\",\"uri\":\"bbbbbbbbbbb\"}," +
                "\"video lesson 1\":{\"mVideoName\":\"video lesson 3\",\"uri\":\"cccccccccc\"}}},\"chapter2\":{\"mChapterName\":\"chapter2\",\"mStringVideoLessonM" +
                "ap\":{\"video lesson 3\":{\"mVideoName\":\"video lesson 1\",\"uri\":\"aaaaaaaaa\"},\"video lesson 2\":{\"mVideoName\":\"video lesson 2\",\"uri\":\"" +
                "bbbbbbbbbbb\"},\"video lesson 1\":{\"mVideoName\":\"video lesson 3\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);


        mFirebaseUtil.mFirestore.collection("branches").document("Fundamentals").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Programming").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Networking").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Artificial").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Databases").set(branch);
    }




}
