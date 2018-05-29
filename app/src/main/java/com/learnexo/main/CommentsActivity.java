package com.learnexo.main;

import android.content.Intent;
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
    private String postId;

    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        doneBtn = findViewById(R.id.doneBtn);
        enterContent = findViewById(R.id.enterContent);
        Intent intent = getIntent();

        publisherId = intent.getStringExtra("EXTRA_PUBLISHER_IDDD");
        postId = intent.getStringExtra("EXTRA_POST_ITEM_ID");


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = enterContent.getText().toString();
                comment = new Comment();
                comment.setComment(content);
                comment.setCommenterId(FirebaseUtil.getCurrentUserId());
                comment.setPublisherId(publisherId);
                comment.setFeedItemId(postId);
                comment.setCommenterName(FeedFragment.sName);
                comment.setCommenterDp(FeedFragment.sDpUrl);

                mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts")
                        .document(postId).collection("comments").add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        mFirebaseUtil.mFirestore.collection("users").document(publisherId).collection("posts")
                                .document(postId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                long comments = (long) documentSnapshot.get("comments");
                                long commentss = comments+1;
                                Map<String, Object> map= new HashMap();
                                map.put("comments",commentss);

                                mFirebaseUtil.mFirestore.collection("users").
                                        document(publisherId).
                                        collection("posts").
                                        document(postId).update(map);

                                finish();
                            }
                        });

                    }

                });

            }
        });

    }
}
