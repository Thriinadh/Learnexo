package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.OverflowMenuListener;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllAnswersActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_CONTENT = "com.learnexo.question_content";
    public static final String EXTRA_QUESTION_TAG = "com.learnexo.question_tag";
    public static final String EXTRA_QUESTIONER_ID = "com.learnexo.questioner_id";
    public static final String EXTRA_ITEM_ID = "com.learnexo.questioner_item_id";
    public static final String EXTRA_QUESTION_IDD = "com.learnexo.question_idd";


    private TextView questionTView;
    private TextView answerBtn;
    private int noOfAns;
    private TextView noOfAnswersView;
    private List<Answer> mAnswers;
    private AllAnswersAdapter mAdapter;
    private ImageView challengeIcon;
    private ImageView quesOverFlow;

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

        setupAnswersRecycler();

        getIntentDetails();

        wireViews();

        getAnswers();

        bindData();

        answerBtnListener();

        User user = new User();
        overflowListener(user);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.insertDeleteBookMarks();
    }

    private void overflowListener(User user) {
        quesOverFlow.setOnClickListener(new OverflowMenuListener(this, user, OverflowType.QUES_CHALLENGE));
    }

    private void answerBtnListener() {
        answerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question question=new Question();
                question.setContent(quesContent);
                question.setFeedItemId(feedItemId);
                question.setTags(Collections.singletonList(questionTag));
                question.setUserId(questionerId);


                Intent intent = AnswerActivity.newIntent(AllAnswersActivity.this, question);
                intent.putExtra("ANSWER_TYPE", is_Challenge);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
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
                    Answer answer=documentSnapshot.toObject(Answer.class);
                    answer.setFeedItemId(documentSnapshot.getId());
                    mAnswers.add(answer);
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
            challengeIcon = findViewById(R.id.challengeIcon);
            challengeIcon.setVisibility(View.VISIBLE);
        }
    }

    private void wireViews() {
        questionTView = findViewById(R.id.questionTView);
        questionTView.setText(quesContent);
        answerBtn = findViewById(R.id.answer);
        noOfAnswersView = findViewById(R.id.totalAnsTView);

        quesOverFlow=findViewById(R.id.quesOverFlow);
    }

    private void setupAnswersRecycler() {
        mAnswers = new ArrayList<>();
        mAdapter = new AllAnswersAdapter(mAnswers);

        RecyclerView ansRecyclerView = findViewById(R.id.allAnsRecycler);
        ansRecyclerView.setLayoutManager(new LinearLayoutManager(AllAnswersActivity.this));
        ansRecyclerView.setAdapter(mAdapter);
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
