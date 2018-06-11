package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.OverflowMenuListener;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class UserQuesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Question> mQuestions;
    private Context mContext;

    private final String mCurrentUserId = FirebaseUtil.getCurrentUserId();

    private boolean isOtherProfile;
    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;
    private boolean isChallenge;

    User mUser;

    public UserQuesRecyclerAdapter(List<Question> mFeedItems, boolean isOtherProfile, String otherProfileId,
                                   String otherProfileName, String  otherProfileDP, boolean isChallenge) {
        this.mQuestions = mFeedItems;

        this.isOtherProfile=isOtherProfile;
        this.otherProfileId=otherProfileId;
        this.otherProfileName=otherProfileName;
        this.otherProfileDP=otherProfileDP;
        this.isChallenge=isChallenge;
        if(isOtherProfile)
            mUser = new User(otherProfileId,otherProfileName,otherProfileDP);
        else
            mUser = new User(mCurrentUserId,FeedFragment.sName,FeedFragment.sDpUrl);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_list_item, parent, false);
        return new AllQuesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question = mQuestions.get(position);

        if (question != null) {

            final String questionn = question.getContent();
            final String timeAgo = convertDateToAgo(question.getPublishTime());

            AllQuesHolder allQuesHolder = (AllQuesHolder) holder;
            allQuesHolder.wireViews();
            bindQuestion(allQuesHolder, questionn, timeAgo, isChallenge);
            allQuestionsOverflowListener(allQuesHolder, question);
            questionListener(allQuesHolder, question);

        }
    }
    private void questionListener(AllQuesHolder holder, final Question question) {

        if(isChallenge)
            holder.questionContent.setOnClickListener(questionChallengeListener(question, FeedItem.CRACK));
        else
            holder.questionContent.setOnClickListener(questionChallengeListener(question, FeedItem.ANSWER));
    }
    @NonNull
    private View.OnClickListener questionChallengeListener(final Question question, final int answerType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AllAnswersActivity.newIntent(mContext, question);
                intent.putExtra("ANSWER_TYPE", answerType);
                mContext.startActivity(intent);
            }
        };
    }
    private void allQuestionsOverflowListener(AllQuesHolder allQuesHolder, Question question) {
        allQuesHolder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, mUser, OverflowType.QUES_CHALLENGE,question));
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    private void bindQuestion(@NonNull AllQuesHolder holder, final String question, String timeAgo, boolean isChallenge) {
        holder.setContent(question);
        holder.setTime(timeAgo);
        if(isChallenge) {
            holder.noAnswerYet.setText("No Cracks Yet.");
            holder.challengeIcon.setVisibility(View.VISIBLE);
        }
    }

    public class AllQuesHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView questionContent;
        private TextView noAnswerYet;
        private TextView answer;
        private TextView followQues;
        private ImageView challengeIcon;
        private TextView answeredTime;
        private ImageView overflowImgView;

        public AllQuesHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            questionContent = mView.findViewById(R.id.questionTView);
            noAnswerYet = mView.findViewById(R.id.noAnswerYet);
            answer = mView.findViewById(R.id.answer);
            followQues = mView.findViewById(R.id.followQues);
            challengeIcon = mView.findViewById(R.id.imageView2);
            answeredTime = mView.findViewById(R.id.postedTime);
            overflowImgView = mView.findViewById(R.id.quesOverFlow);
        }

        public void setContent(String question) {
            questionContent.setText(question);

        }

        public void setTime(String timeAgo) {
            this.answeredTime.setText(timeAgo);
        }

    }

}
