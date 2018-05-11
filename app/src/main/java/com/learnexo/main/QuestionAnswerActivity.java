package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class QuestionAnswerActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION = "com.learnexo.questiondata";

    private TextView askedQuestion;
    private EditText quesAns;
    private TextView submitTView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        quesAns = findViewById(R.id.quesAns);
        submitTView = findViewById(R.id.submitTView);

        Intent intent = getIntent();
        String quesData = intent.getStringExtra(EXTRA_QUESTION);
        askedQuestion = findViewById(R.id.questionView);
        askedQuestion.setText(quesData);

        enablePublishBtn();

    }

    public static Intent newIntent(Context context, String question) {

        Intent intent = new Intent(context, QuestionAnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION, question);
        return intent;
    }

    private void enablePublishBtn() {
        quesAns.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //...
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //...
            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean isReady = quesAns.getText().toString().length() > 3;
                submitTView.setEnabled(isReady);
                if(isReady) {
                    submitTView.setEnabled(true);
                } else  {
                    submitTView.setEnabled(false);
                }
            }
        });
    }

}
