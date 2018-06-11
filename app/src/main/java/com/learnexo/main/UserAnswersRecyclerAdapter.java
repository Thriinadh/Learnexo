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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.OverflowMenuListener;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class UserAnswersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Answer> mAnswers;
    private Context mContext;

    private final String mCurrentUserId = FirebaseUtil.getCurrentUserId();

    private boolean isOtherProfile;
    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;
    private boolean is_crack;

    User mUser;

    public UserAnswersRecyclerAdapter(List<Answer> mFeedItems, boolean isOtherProfile, String otherProfileId,
                                      String otherProfileName, String  otherProfileDP, boolean is_crack) {
        this.mAnswers = mFeedItems;
        this.isOtherProfile=isOtherProfile;
        this.otherProfileId=otherProfileId;
        this.otherProfileName=otherProfileName;
        this.otherProfileDP=otherProfileDP;
        this.is_crack=is_crack;
        if(isOtherProfile)
            mUser = new User(otherProfileId,otherProfileName,otherProfileDP);
        else
            mUser = new User(mCurrentUserId,FeedFragment.sName,FeedFragment.sDpUrl);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.crack_list_item, parent, false);
        return new AllAnswersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Answer answer = mAnswers.get(position);

        if (answer != null) {
            User publisher =new User();
            publisher.setUserId(answer.getUserId());
            publisher.setFirstName(FeedFragment.sName);
            publisher.setDpUrl(FeedFragment.sDpUrl);


            final int type = answer.getType();
            final String question = answer.getQuesContent();
            final String itemContent = answer.getContent();
            final String imagePosted = answer.getImgUrl();
            final String imageThumb = answer.getImgThmb();
            final String timeAgo = convertDateToAgo(answer.getPublishTime());
            long views = answer.getViews();
            long upVotes = answer.getUpVotes();
            long comments = answer.getComments();

            AllAnswersHolder allAnswersHolder = (AllAnswersHolder) holder;
            allAnswersHolder.wireViews();
            bindAnswer(allAnswersHolder, question, itemContent, imagePosted, imageThumb, timeAgo, type);
            allAnswersOverflowListener(allAnswersHolder, answer);
            answerContentListener(allAnswersHolder, itemContent, question, imagePosted, imageThumb,
                    timeAgo, publisher, answer.getQuesId(), answer.getTags(), answer.getFeedItemId(), comments, views, upVotes);

        }
    }

    private void allAnswersOverflowListener(AllAnswersHolder allAnswersHolder, Answer answer) {
        allAnswersHolder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, mUser, OverflowType.POST_ANS_CRACK, answer));
    }

    private void answerContentListener(@NonNull AllAnswersHolder holder, final String itemContent,
                                       final String questionAsked, final String imagePosted, final String imageThumb,
                                       final String timeAgo, final User publisher, final String questionId, final List<String> tags, final String ansId,
                                       final long comments, final long ansViews, final long ansUpvotes) {
        holder.questionContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(itemContent, questionAsked, imagePosted, imageThumb, timeAgo, publisher,
                        questionId, is_crack, tags.get(0), ansId, comments, ansViews, ansUpvotes);
            }
        });
        holder.answerContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(itemContent, questionAsked, imagePosted, imageThumb, timeAgo, publisher,
                        questionId, is_crack, tags.get(0), ansId, comments, ansViews, ansUpvotes);
            }
        });
    }
    private void gotoFullAnswerActivity(String challenge, String itemContent, String imagePosted, String imageThumb,
                                        String timeAgo, User publisher, String questionId, boolean is_crack, String tag,
                                        String ansId, long comments, long ansViews, long ansUpvotes) {
        Intent intent = FullAnswerActivity.newIntent(mContext, itemContent, challenge, imagePosted, imageThumb,
                timeAgo, publisher, questionId, tag, ansId, comments, ansViews, ansUpvotes);
        if(is_crack)
            intent.putExtra("IS_CRACK",true);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    private void bindAnswer(@NonNull AllAnswersHolder holder, final String question, final String answer, final String publishedImg,
                            final String publishedThumb, String timeAgo, int type) {
        holder.setContent(answer, question);
        holder.setType(type);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
        holder.setUserData(mUser.getFirstName(), mUser.getDpUrl());
    }


    public class AllAnswersHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView questionContent;
        private TextView answerContent;
        private TextView userName;
        private CircleImageView circleImageView;
        private TextView answeredTime;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private ImageView challengeIcon;
        private TextView seeMore;

        public AllAnswersHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            questionContent = mView.findViewById(R.id.questionTView);
            circleImageView = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            postedImgView = mView.findViewById(R.id.postedImagee);
            challengeIcon = mView.findViewById(R.id.imageView4);
            answeredTime = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);
            answerContent = mView.findViewById(R.id.answerContent);
            seeMore = mView.findViewById(R.id.seeMore);
        }

        public void setContent(String answer, String question) {
            answerContent.setText(answer);
            questionContent.setText(question);
            answerContent.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = answerContent.getLineCount();
                    if(lineCount>=3)
                        seeMore.setVisibility(View.VISIBLE);
                }
            });

        }
        public void setAnsImgView(String imageUrl, String imageThumb) {
            if(imageUrl != null&&mContext!=null) {
                postedImgView.setVisibility(View.VISIBLE);
                Glide.with(mContext.getApplicationContext()).load(imageUrl)
                        .thumbnail(Glide.with(mContext.getApplicationContext()).load(imageThumb))
                        .into(postedImgView);
            }
        }

        public void setTime(String timeAgo) {
            this.answeredTime.setText(timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext.getApplicationContext()).load(image).apply(placeholderOption).into(circleImageView);

        }
        public  void setType(int type) {
            if(type == 1)
                challengeIcon.setVisibility(View.INVISIBLE);
        }

    }

}
