package com.learnexo.main;

import android.os.AsyncTask;
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

        new BookMarksFetcher().execute(FirebaseUtil.getCurrentUserId());

    }




    public class BookMarksFetcher extends AsyncTask<String, Void, List<FeedItem>>{

        @Override
        protected List<FeedItem> doInBackground(String... strings) {
            String currentUserId = strings[0];
            mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("bookmarks").
                    get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){

                        Bookmark bookmark = queryDocumentSnapshot.toObject(Bookmark.class);
                        String bookMarkItemId = bookmark.getBookMarkItemId();
                        String publisherId = bookmark.getPublisherId();

                        if (bookmark.getBookMarkType().name().equals("POST"))
                            new FeedItemFetcher().execute(publisherId,bookMarkItemId,"posts");
                        else
                            new FeedItemFetcher().execute(publisherId,bookMarkItemId,"answers");
                    }

                }
            });


            return null;
        }
    }





    public class FeedItemFetcher extends AsyncTask<String, Void, FeedItem>{

        @Override
        protected FeedItem doInBackground(String... strings) {
            String publisherId = strings[0];
            String feedItemId = strings[1];
            final String path = strings[2];
            final FeedItem[] feedItem = new FeedItem[1];

            mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection(path).document(feedItemId)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(path.equals("posts")){
                        feedItem[0] =documentSnapshot.toObject(Post.class);
                    }else{
                        feedItem[0]=(documentSnapshot.toObject(Answer.class));
                    }
                }
            });

            return feedItem[0];
        }


        @Override
        protected void onPostExecute(FeedItem feedItem) {
            super.onPostExecute(feedItem);

            notifyAdapter(feedItem);
        }
    }

    private void notifyAdapter(FeedItem feedItem) {
        if(feedItem!=null)
        mFeedItems.add(feedItem);
        mAdapter.notifyDataSetChanged();
    }


}
