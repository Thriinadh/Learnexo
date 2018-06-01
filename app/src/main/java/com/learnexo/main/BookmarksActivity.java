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
import com.learnexo.model.video.Branch;
import com.learnexo.model.video.Subject;
import com.learnexo.model.video.VideoLesson;
import com.learnexo.model.video.chapter.Chapter;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Branch branch=new Branch();
        branch.setName("Fundamentals");
        Map<String,Subject> stringSubjectMap=new HashMap<>();

        Subject subject1=new Subject();
        subject1.setName("OS");
        subject1.setPrice(123.60);
        stringSubjectMap.put("OS",subject1);

        Chapter chapter = new Chapter();
        chapter.setName("chapter1");
        VideoLesson videoLesson = new VideoLesson();
        videoLesson.setName("video lesson 1");
        videoLesson.setUri("fsdghkjlh");
        Map<String, VideoLesson> stringVideoLessonMap=new HashMap<>();
        stringVideoLessonMap.put("video lesson 1",videoLesson);
        chapter.setStringVideoLessonMap(stringVideoLessonMap);
        Map<String, Chapter> stringChapterMap = new HashMap<>();
        stringChapterMap.put("chapter1", chapter);
        subject1.setStringChapterMap(stringChapterMap);

        VideoLesson videoLessonn = new VideoLesson();
        videoLessonn.setName("video lesson 2");
        videoLessonn.setUri("fsdghkjjhfuyjhlh");

        stringVideoLessonMap.put("video lesson 2",videoLesson);
        chapter.setStringVideoLessonMap(stringVideoLessonMap);
        subject1.setStringChapterMap(stringChapterMap);

        VideoLesson videoLessonnn = new VideoLesson();
        videoLessonnn.setName("video lesson 3");
        videoLessonnn.setUri("fsdghkjjhfuyjgchlh");

        stringVideoLessonMap.put("video lesson 3",videoLesson);
        chapter.setStringVideoLessonMap(stringVideoLessonMap);
        subject1.setStringChapterMap(stringChapterMap);

///////////////////////////////////////////////////////////////////////////////////////////////
        Subject subject2=new Subject();
        subject2.setName("CO");
        subject2.setPrice(222222.60);
        stringSubjectMap.put("CO",subject2);


        Chapter chapter2 = new Chapter();
        chapter2.setName("chapter1");
        VideoLesson videoLesson2 = new VideoLesson();
        videoLesson2.setName("video lesson 1");
        videoLesson2.setUri("fsdghkjlh");
        Map<String, VideoLesson> stringVideoLessonMap2=new HashMap<>();
        stringVideoLessonMap2.put("video lesson 1",videoLesson2);
        chapter2.setStringVideoLessonMap(stringVideoLessonMap2);
        Map<String, Chapter> stringChapterMap2 = new HashMap<>();
        stringChapterMap2.put("chapter1", chapter2);
        subject2.setStringChapterMap(stringChapterMap2);

        VideoLesson videoLesson22 = new VideoLesson();
        videoLesson22.setName("video lesson 2");
        videoLesson22.setUri("fsdghkjlgfjh");

        stringVideoLessonMap2.put("video lesson 2",videoLesson2);
        chapter2.setStringVideoLessonMap(stringVideoLessonMap2);
        subject2.setStringChapterMap(stringChapterMap2);

        VideoLesson videoLesson222 = new VideoLesson();
        videoLesson222.setName("video lesson 3");
        videoLesson222.setUri("fsdghkhjbvjhjlgfjh");

        stringVideoLessonMap2.put("video lesson 3",videoLesson2);
        chapter2.setStringVideoLessonMap(stringVideoLessonMap2);
        subject2.setStringChapterMap(stringChapterMap2);

        ////////////////////////////////////////////////////////////////
        Subject subject3=new Subject();
        subject3.setName("CPP");
        subject3.setPrice(333.30);
        stringSubjectMap.put("CPP",subject3);

        Chapter chapter3 = new Chapter();
        chapter3.setName("chapter1");
        VideoLesson videoLesson3 = new VideoLesson();
        videoLesson3.setName("video lesson 1");
        videoLesson3.setUri("fsdghkjlh");
        Map<String, VideoLesson> stringVideoLessonMap3=new HashMap<>();
        stringVideoLessonMap3.put("video lesson 1",videoLesson2);
        chapter2.setStringVideoLessonMap(stringVideoLessonMap3);
        Map<String, Chapter> stringChapterMap3 = new HashMap<>();
        stringChapterMap3.put("chapter1", chapter3);
        subject3.setStringChapterMap(stringChapterMap3);

        VideoLesson videoLesson223 = new VideoLesson();
        videoLesson223.setName("video lesson 2");
        videoLesson223.setUri("fsdghkjlgfjh");

        stringVideoLessonMap3.put("video lesson 2",videoLesson2);
        chapter3.setStringVideoLessonMap(stringVideoLessonMap2);
        subject3.setStringChapterMap(stringChapterMap2);

        VideoLesson videoLesson2222 = new VideoLesson();
        videoLesson2222.setName("video lesson 3");
        videoLesson2222.setUri("fsdghkhjbvjhjlgfjh");

        stringVideoLessonMap2.put("video lesson 3",videoLesson3);
        chapter3.setStringVideoLessonMap(stringVideoLessonMap3);
        subject3.setStringChapterMap(stringChapterMap3);

        /////////////////////////////////////////////////////////
        Subject s4 = new Subject();
        s4.setName("AlGOR");
        stringSubjectMap.put("ALGOR", s4);

        Subject s5 = new Subject();
        s5.setName("BASICS");
        stringSubjectMap.put("BASICS", s5);

        branch.setStringSubjectMap(stringSubjectMap);

        mFirebaseUtil.mFirestore.collection("branches").document("Fundamentals").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Programming").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Networking").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Artificial").set(branch);
        mFirebaseUtil.mFirestore.collection("branches").document("Databases").set(branch);
    }




}
