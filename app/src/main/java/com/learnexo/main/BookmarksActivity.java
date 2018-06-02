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

                "{\"mBranchName\":\"Fundamentals\",\"mSubjectMap\":{\"Computer Programming\":{\"mSubjectName\":\"Computer Programming\",\"isChecked\":false,\"mPrice\":123.6,\"mChapterMap\":{\"Introduction\":{\"mChapterName\":\"Introduction\",\"mVideoLessonMap\":{\"Computer systems & environments\":{\"mVideoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"mVideoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"mVideoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"mChapterName\":\"Functions\",\"mVideoLessonMap\":{\"All kinds of Functions\":{\"mVideoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"mVideoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"mVideoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"mChapterName\":\"Pointers\",\"mVideoLessonMap\":{\"Introduction to Pointers\":{\"mVideoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"mVideoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"mVideoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"mChapterName\":\"Structures & Unions\",\"mVideoLessonMap\":{\"Input and Output\":{\"mVideoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"mVideoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"mVideoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"mSubjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"mPrice\":123.6,\"mChapterMap\":{\"Mathematical logic\":{\"mChapterName\":\"Mathematical logic\",\"mVideoLessonMap\":{\"Statements & notions\":{\"mVideoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"mVideoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"mVideoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"mChapterName\":\"Elementory combinatorics\",\"mSideoLessonMap\":{\"Combinations & Permutations\":{\"mVideoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"mVideoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"mVideoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"mChapterName\":\"Recurrence relation\",\"mSideoLessonMap\":{\"Generating Functions & Funds\":{\"mVideoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"mVideoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"mVideoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"mSubjectName\":\"Data Structures\",\"isChecked\":false,\"mPrice\":123.6,\"mChapterMap\":{\"Basic concepts\":{\"mChapterName\":\"Basic concepts\",\"mVideoLessonMap\":{\"Data abstraction performance\":{\"mVideoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"mVideoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"mVideoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"mChapterName\":\"Stack ADT\",\"mVideoLessonMap\":{\"Recursion Implementation\":{\"mVideoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"mVideoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"mVideoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"mChapterName\":\"searching\",\"mVideoLessonMap\":{\"Overflow Handling\":{\"mVideoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"mVideoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"mVideoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"mChapterName\":\"Search Trees\",\"mVideoLessonMap\":{\"Definition & Examples\":{\"mVideoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"mVideoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"mVideoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);


        mFirebaseUtil.mFirestore.collection("branches").document("Fundamentals").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Programming").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Networking").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Artificial").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Databases").set(branch);
    }




}
