package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.question.Question;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class AllAnswersActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_CONTENT = "com.learnexo.question_content";
    public static final String EXTRA_QUESTION_TAG = "com.learnexo.question_tag";
    public static final String EXTRA_QUESTIONER_ID = "com.learnexo.questioner_id";
    public static final String EXTRA_ITEM_ID = "com.learnexo.questioner_item_id";

    private TextView questionTView;
    private TextView answerBtn;
    private int noOfAns;
    private TextView noOfAnswersView;
    private List<Answer> mAnswers;
    private AllAnsRecyclerAdapter mAdapter;

    private String quesContent;
    private String questionTag;
    private String questionerId;
    private String feedItemId;
    private int is_Challenge;

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_answers);


        setupRecyclerView();

        getIntentDetails();

        wireViews();


        getAnswers();


        bindData();
    }

    private void getAnswers() {
        mFirebaseUtil.mFirestore.collection("questions").document(feedItemId)
                .collection("answers").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                noOfAns=documents.size();
                bindNoOfAns();
                for(DocumentSnapshot documentSnapshot:documents){
                    mAnswers.add(documentSnapshot.toObject(Answer.class));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void bindNoOfAns() {
        if(is_Challenge== FeedItem.CRACK) {
            if(noOfAns!=1)
                noOfAnswersView.setText(noOfAns+" Cracks");
            else
                noOfAnswersView.setText(noOfAns+" Crack");
        }else
            if(noOfAns!=1)
                noOfAnswersView.setText(noOfAns+" Answers");
            else
                noOfAnswersView.setText(noOfAns+" Answer");
    }

    private void bindData() {
        if(is_Challenge== FeedItem.CRACK) {
            answerBtn.setText("Crack");
        }
    }

    private void wireViews() {
        questionTView = findViewById(R.id.questionTView);
        questionTView.setText(quesContent);
        answerBtn = findViewById(R.id.answer);
        noOfAnswersView = findViewById(R.id.totalAnsTView);
    }

    private void setupRecyclerView() {
        mAnswers = new ArrayList<>();
        mAdapter = new AllAnsRecyclerAdapter(mAnswers);

        RecyclerView feedRecyclerView = findViewById(R.id.allAnsRecycler);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(AllAnswersActivity.this));
        feedRecyclerView.setAdapter(mAdapter);
    }

    private void getIntentDetails() {
        Intent intent = getIntent();
        quesContent = intent.getStringExtra(EXTRA_QUESTION_CONTENT);
        questionTag = intent.getStringExtra(EXTRA_QUESTION_TAG);
        questionerId = intent.getStringExtra(EXTRA_QUESTIONER_ID);
        feedItemId = intent.getStringExtra(EXTRA_ITEM_ID);
        is_Challenge=intent.getIntExtra("ANSWER_TYPE",10);
    }



    public static Intent newIntent(Context context, Question question) {

        Intent intent = new Intent(context, AllAnswersActivity.class);
        intent.putExtra(EXTRA_QUESTION_CONTENT, question.getContent());
        intent.putExtra(EXTRA_QUESTION_TAG,question.getTags().get(0));
        intent.putExtra(EXTRA_QUESTIONER_ID,question.getUserId());
        intent.putExtra(EXTRA_ITEM_ID, question.getFeedItemId());
        return intent;
    }
}
