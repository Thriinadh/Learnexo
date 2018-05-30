package com.learnexo.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.model.feed.likediv.Comment;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {

    private TextView doneBtn;
    private EditText enterContent;
    Comment comment;
    private String publisherId;
    private String feedItemId;

    private String questionId;

    boolean isFromFullAnswer;

    final String comments="comments";
    final String users = "users";
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        wireViews();

        handleIntent();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = enterContent.getText().toString();
                comment = new Comment();
                comment.setComment(content);
                comment.setCommenterId(FirebaseUtil.getCurrentUserId());
                comment.setCommenterName(FeedFragment.sName);
                comment.setCommenterDp(FeedFragment.sDpUrl);
                comment.setPublisherId(publisherId);
                comment.setFeedItemId(feedItemId);


                if(!isFromFullAnswer) {
                    handlePosts(comments, users);
                }
                else {
                    handleAnswers(comments, users);
                }
            }
        });

    }

    private void handleAnswers(final String comments, final String users) {
        final String questions = "questions";
        final String answers = "answers";

        mFirebaseUtil.mFirestore.collection(questions).document(questionId)
                .collection(answers).document(feedItemId).collection(comments)
                .add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                mFirebaseUtil.mFirestore.collection(users).document(publisherId)
                        .collection(answers).document(feedItemId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                Map<String, Object> map = getStringObjectMap(documentSnapshot, comments);

                                mFirebaseUtil.mFirestore.collection(users).document(publisherId)
                                        .collection(answers).document(feedItemId).update(map);

                                mFirebaseUtil.mFirestore.collection(questions).document(questionId)
                                        .collection(answers).document(feedItemId).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                Map<String, Object> map = getStringObjectMap(documentSnapshot, comments);

                                                mFirebaseUtil.mFirestore.collection(questions).document(questionId)
                                                        .collection(answers).document(feedItemId).update(map);

                                                finish();

                                            }
                                        });

                            }
                        });

            }
        });
    }

    private void handlePosts(final String comments, final String users) {
        final String posts = "posts";


        mFirebaseUtil.mFirestore.collection(users).document(publisherId)
                .collection(posts).document(feedItemId).collection(comments)
                .add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                mFirebaseUtil.mFirestore.collection(users).document(publisherId).collection(posts)
                        .document(feedItemId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Map<String, Object> map = getStringObjectMap(documentSnapshot, comments);

                        mFirebaseUtil.mFirestore.collection(users).
                                document(publisherId).
                                collection(posts).
                                document(feedItemId).update(map);

                        finish();
                    }
                });

            }

        });
    }

    @NonNull
    private Map<String, Object> getStringObjectMap(DocumentSnapshot documentSnapshot, String comments) {
        long noOfComments = (long) documentSnapshot.get(comments);
        noOfComments = noOfComments + 1;
        Map<String, Object> map = new HashMap();
        map.put(comments, noOfComments);
        return map;
    }

    private void handleIntent() {
        Intent intent = getIntent();
        publisherId = intent.getStringExtra("EXTRA_PUBLISHER_IDDD");
        feedItemId = intent.getStringExtra("EXTRA_FEED_ITEM_ID");
        isFromFullAnswer = intent.getBooleanExtra("IF_FROM_FULL_ANSWER_ACTIVITY", false);
        questionId = intent.getStringExtra("EXTRA_QUESTION_ID");
    }

    private void wireViews() {
        doneBtn = findViewById(R.id.doneBtn);
        enterContent = findViewById(R.id.enterContent);
    }
}
