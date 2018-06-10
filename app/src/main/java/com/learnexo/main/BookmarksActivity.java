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

                "{\"branchName\":\"Fundamentals\",\"subjectMap\":{\"Computer Programming\":{\"subjectName\":\"Computer Programming\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Introduction\":{\"chapterName\":\"Introduction\",\"videoLessonMap\":{\"Computer systems & environments\":{\"videoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"videoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"chapterName\":\"Functions\",\"videoLessonMap\":{\"All kinds of Functions\":{\"videoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"videoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"videoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"chapterName\":\"Pointers\",\"videoLessonMap\":{\"Introduction to Pointers\":{\"videoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"videoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"videoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"chapterName\":\"Structures & Unions\",\"videoLessonMap\":{\"Input and Output\":{\"videoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"videoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"videoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"subjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Mathematical logic\":{\"chapterName\":\"Mathematical logic\",\"videoLessonMap\":{\"Statements & notions\":{\"videoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"videoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"videoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"chapterName\":\"Elementory combinatorics\",\"videoLessonMap\":{\"Combinations & Permutations\":{\"videoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"videoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"videoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"chapterName\":\"Recurrence relation\",\"videoLessonMap\":{\"Generating Functions & Funds\":{\"videoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"videoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"videoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"subjectName\":\"Data Structures\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Basic concepts\":{\"chapterName\":\"Basic concepts\",\"videoLessonMap\":{\"Data abstraction performance\":{\"videoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"videoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"chapterName\":\"Stack ADT\",\"videoLessonMap\":{\"Recursion Implementation\":{\"videoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"videoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"videoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"chapterName\":\"searching\",\"videoLessonMap\":{\"Overflow Handling\":{\"videoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"videoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"videoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"chapterName\":\"Search Trees\",\"videoLessonMap\":{\"Definition & Examples\":{\"videoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"videoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"videoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);

        Branch branch2 = gson.fromJson(

                "{\"branchName\":\"Programming\",\"subjectMap\":{\"Computer Programming\":{\"subjectName\":\"Computer Programming\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Introduction\":{\"chapterName\":\"Introduction\",\"videoLessonMap\":{\"Computer systems & environments\":{\"videoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"videoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"chapterName\":\"Functions\",\"videoLessonMap\":{\"All kinds of Functions\":{\"videoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"videoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"videoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"chapterName\":\"Pointers\",\"videoLessonMap\":{\"Introduction to Pointers\":{\"videoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"videoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"videoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"chapterName\":\"Structures & Unions\",\"videoLessonMap\":{\"Input and Output\":{\"videoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"videoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"videoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"subjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Mathematical logic\":{\"chapterName\":\"Mathematical logic\",\"videoLessonMap\":{\"Statements & notions\":{\"videoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"videoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"videoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"chapterName\":\"Elementory combinatorics\",\"videoLessonMap\":{\"Combinations & Permutations\":{\"videoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"videoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"videoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"chapterName\":\"Recurrence relation\",\"videoLessonMap\":{\"Generating Functions & Funds\":{\"videoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"videoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"videoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"subjectName\":\"Data Structures\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Basic concepts\":{\"chapterName\":\"Basic concepts\",\"videoLessonMap\":{\"Data abstraction performance\":{\"videoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"videoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"chapterName\":\"Stack ADT\",\"videoLessonMap\":{\"Recursion Implementation\":{\"videoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"videoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"videoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"chapterName\":\"searching\",\"videoLessonMap\":{\"Overflow Handling\":{\"videoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"videoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"videoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"chapterName\":\"Search Trees\",\"videoLessonMap\":{\"Definition & Examples\":{\"videoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"videoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"videoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);

        Branch branch3 = gson.fromJson(

                "{\"branchName\":\"Networking\",\"subjectMap\":{\"Computer Programming\":{\"subjectName\":\"Computer Programming\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Introduction\":{\"chapterName\":\"Introduction\",\"videoLessonMap\":{\"Computer systems & environments\":{\"videoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"videoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"chapterName\":\"Functions\",\"videoLessonMap\":{\"All kinds of Functions\":{\"videoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"videoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"videoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"chapterName\":\"Pointers\",\"videoLessonMap\":{\"Introduction to Pointers\":{\"videoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"videoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"videoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"chapterName\":\"Structures & Unions\",\"videoLessonMap\":{\"Input and Output\":{\"videoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"videoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"videoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"subjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Mathematical logic\":{\"chapterName\":\"Mathematical logic\",\"videoLessonMap\":{\"Statements & notions\":{\"videoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"videoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"videoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"chapterName\":\"Elementory combinatorics\",\"videoLessonMap\":{\"Combinations & Permutations\":{\"videoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"videoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"videoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"chapterName\":\"Recurrence relation\",\"videoLessonMap\":{\"Generating Functions & Funds\":{\"videoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"videoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"videoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"subjectName\":\"Data Structures\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Basic concepts\":{\"chapterName\":\"Basic concepts\",\"videoLessonMap\":{\"Data abstraction performance\":{\"videoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"videoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"chapterName\":\"Stack ADT\",\"videoLessonMap\":{\"Recursion Implementation\":{\"videoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"videoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"videoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"chapterName\":\"searching\",\"videoLessonMap\":{\"Overflow Handling\":{\"videoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"videoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"videoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"chapterName\":\"Search Trees\",\"videoLessonMap\":{\"Definition & Examples\":{\"videoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"videoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"videoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);

        Branch branch4 = gson.fromJson(

                "{\"branchName\":\"Artificial\",\"subjectMap\":{\"Computer Programming\":{\"subjectName\":\"Computer Programming\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Introduction\":{\"chapterName\":\"Introduction\",\"videoLessonMap\":{\"Computer systems & environments\":{\"videoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"videoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"chapterName\":\"Functions\",\"videoLessonMap\":{\"All kinds of Functions\":{\"videoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"videoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"videoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"chapterName\":\"Pointers\",\"videoLessonMap\":{\"Introduction to Pointers\":{\"videoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"videoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"videoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"chapterName\":\"Structures & Unions\",\"videoLessonMap\":{\"Input and Output\":{\"videoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"videoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"videoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"subjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Mathematical logic\":{\"chapterName\":\"Mathematical logic\",\"videoLessonMap\":{\"Statements & notions\":{\"videoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"videoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"videoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"chapterName\":\"Elementory combinatorics\",\"videoLessonMap\":{\"Combinations & Permutations\":{\"videoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"videoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"videoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"chapterName\":\"Recurrence relation\",\"videoLessonMap\":{\"Generating Functions & Funds\":{\"videoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"videoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"videoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"subjectName\":\"Data Structures\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Basic concepts\":{\"chapterName\":\"Basic concepts\",\"videoLessonMap\":{\"Data abstraction performance\":{\"videoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"videoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"chapterName\":\"Stack ADT\",\"videoLessonMap\":{\"Recursion Implementation\":{\"videoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"videoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"videoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"chapterName\":\"searching\",\"videoLessonMap\":{\"Overflow Handling\":{\"videoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"videoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"videoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"chapterName\":\"Search Trees\",\"videoLessonMap\":{\"Definition & Examples\":{\"videoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"videoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"videoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);

        Branch branch5 = gson.fromJson(

                "{\"branchName\":\"Databases\",\"subjectMap\":{\"Computer Programming\":{\"subjectName\":\"Computer Programming\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Introduction\":{\"chapterName\":\"Introduction\",\"videoLessonMap\":{\"Computer systems & environments\":{\"videoName\":\"Computer systems & environments\",\"uri\":\"aaaaaaaaa\"},\"Introduction to C language\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Conditions & loops\":{\"videoName\":\"Conditions & loops\",\"uri\":\"cccccccccc\"}}},\"Functions\":{\"chapterName\":\"Functions\",\"videoLessonMap\":{\"All kinds of Functions\":{\"videoName\":\"All kinds of Functions\",\"uri\":\"aaaaaaaaa\"},\"Preprocessors\":{\"videoName\":\"Preprocessors\",\"uri\":\"bbbbbbbbbbb\"},\"Arrays\":{\"videoName\":\"Arrays\",\"uri\":\"cccccccccc\"}}},\"Pointers\":{\"chapterName\":\"Pointers\",\"videoLessonMap\":{\"Introduction to Pointers\":{\"videoName\":\"Introduction to Pointers\",\"uri\":\"aaaaaaaaa\"},\"Memory allocation functions\":{\"videoName\":\"Memory allocation functions\",\"uri\":\"bbbbbbbbbbb\"},\"Strings\":{\"videoName\":\"Strings\",\"uri\":\"cccccccccc\"}}},\"Structures & Unions\":{\"chapterName\":\"Structures & Unions\",\"videoLessonMap\":{\"Input and Output\":{\"videoName\":\"Input and Output\",\"uri\":\"aaaaaaaaa\"},\"Searching & Sorting\":{\"videoName\":\"Searching & Sorting\",\"uri\":\"bbbbbbbbbbb\"},\"Lists\":{\"videoName\":\"Lists\",\"uri\":\"cccccccccc\"}}}}},\"Math Foundations of CS\":{\"subjectName\":\"Math Foundations of CS\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Mathematical logic\":{\"chapterName\":\"Mathematical logic\",\"videoLessonMap\":{\"Statements & notions\":{\"videoName\":\"Statements & notions\",\"uri\":\"aaaaaaaaa\"},\"Predicates\":{\"videoName\":\"Predicates\",\"uri\":\"bbbbbbbbbbb\"},\"Relations & Structures\":{\"videoName\":\"Relations & Structures\",\"uri\":\"cccccccccc\"}}},\"Elementory combinatorics\":{\"chapterName\":\"Elementory combinatorics\",\"videoLessonMap\":{\"Combinations & Permutations\":{\"videoName\":\"Combinations & Permutations\",\"uri\":\"aaaaaaaaa\"},\"Pegion hole principles\":{\"videoName\":\"Pegion hole principles\",\"uri\":\"bbbbbbbbbbb\"},\"Binomial Theorems\":{\"videoName\":\"Binomial Theorems\",\"uri\":\"cccccccccc\"}}},\"Recurrence relation\":{\"chapterName\":\"Recurrence relation\",\"videoLessonMap\":{\"Generating Functions & Funds\":{\"videoName\":\"Generating Functions & Funds\",\"uri\":\"aaaaaaaaa\"},\"Graph Theory\":{\"videoName\":\"Graph Theory\",\"uri\":\"bbbbbbbbbbb\"},\"Euler circuits\":{\"videoName\":\"Euler circuits\",\"uri\":\"cccccccccc\"}}}}},\"Data Structures\":{\"subjectName\":\"Data Structures\",\"isChecked\":false,\"price\":123.6,\"chapterMap\":{\"Basic concepts\":{\"chapterName\":\"Basic concepts\",\"videoLessonMap\":{\"Data abstraction performance\":{\"videoName\":\"Data abstraction performance\",\"uri\":\"aaaaaaaaa\"},\"Linear & Non-Linear DS\":{\"videoName\":\"Introduction to C language\",\"uri\":\"bbbbbbbbbbb\"},\"Linked lists\":{\"videoName\":\"Linked lists\",\"uri\":\"cccccccccc\"}}},\"Stack ADT\":{\"chapterName\":\"Stack ADT\",\"videoLessonMap\":{\"Recursion Implementation\":{\"videoName\":\"Recursion Implementation\",\"uri\":\"aaaaaaaaa\"},\"Arrays & Linked lists in C\":{\"videoName\":\"Arrays & Linked lists in C\",\"uri\":\"bbbbbbbbbbb\"},\"Trees\":{\"videoName\":\"Trees\",\"uri\":\"cccccccccc\"}}},\"Searching\":{\"chapterName\":\"searching\",\"videoLessonMap\":{\"Overflow Handling\":{\"videoName\":\"Overflow Handling\",\"uri\":\"aaaaaaaaa\"},\"Selection Sorting & others\":{\"videoName\":\"Selection Sorting & others\",\"uri\":\"bbbbbbbbbbb\"},\"Comparison of sorting methods\":{\"videoName\":\"Comparison of sorting methods\",\"uri\":\"cccccccccc\"}}},\"Search Trees\":{\"chapterName\":\"Search Trees\",\"videoLessonMap\":{\"Definition & Examples\":{\"videoName\":\"Definition & Examples\",\"uri\":\"aaaaaaaaa\"},\"Red-Black & Splay Trees\":{\"videoName\":\"Red-Black & Splay Trees\",\"uri\":\"bbbbbbbbbbb\"},\"The Knuth-Morris-Pratt algorithm\":{\"videoName\":\"The Knuth-Morris-Pratt algorithm\",\"uri\":\"cccccccccc\"}}}}}}}"

                ,Branch.class);


        mFirebaseUtil.mFirestore.collection("branches").document("Fundamentals").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Programming").set(branch2);
        mFirebaseUtil.mFirestore.collection("branches").document("Networking").set(branch3);
        mFirebaseUtil.mFirestore.collection("branches").document("Artificial").set(branch4);
        mFirebaseUtil.mFirestore.collection("branches").document("Databases").set(branch5);
    }


}
