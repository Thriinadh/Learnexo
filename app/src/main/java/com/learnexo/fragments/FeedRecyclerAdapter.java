package com.learnexo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.main.AllAnswersActivity;
import com.learnexo.main.AnswerActivity;
import com.learnexo.main.FullAnswerActivity;
import com.learnexo.main.FullPostActivity;
import com.learnexo.main.OthersProfileActivity;
import com.learnexo.main.R;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    BottomSheetDialog mDialog;
    private List<FeedItem> mFeedItems;
    private Context mContext;
    private FirebaseUtil mFireBaseUtil = new FirebaseUtil();

    public FeedRecyclerAdapter(List<FeedItem> mFeedItems) {
        this.mFeedItems = mFeedItems;
    }

    @Override
    public int getItemViewType(int position) {
        int type=0;
        if(mFeedItems.get(position)!=null)
            type = mFeedItems.get(position).getType();

        switch (type) {
            case 0:
                return FeedItem.POST;
            case 1:
                return FeedItem.ANSWER;
            case 2:
                return FeedItem.CRACK;
            case 3:
                return FeedItem.QUESTION;
            case 4:
                return FeedItem.CHALLENGE;
            default:
                return -1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view;

        switch (viewType) {
            case FeedItem.POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
                return new PostHolder(view);

            case FeedItem.ANSWER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ans_list_item, parent, false);
                return new AnswerHolder(view);

            case FeedItem.CRACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crack_list_item, parent, false);
                return new CrackHolder(view);

            case FeedItem.QUESTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item, parent, false);
                return new QuestionHolder(view);

            case FeedItem.CHALLENGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_list_item, parent, false);
                return new ChallengeHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        FeedItem feedItem= mFeedItems.get(position);


        if(feedItem!=null) {

            User publisher = new User();
            final String feedItemId = feedItem.getFeedItemId();
            final String publisherId = feedItem.getUserId();
            final String itemContent = feedItem.getContent();
            final String imagePosted = feedItem.getImgUrl();
            final String imageThumb = feedItem.getImgThmb();
            final String tag = feedItem.getTags().get(0);

            final long comments = feedItem.getComments();
            final String timeAgo = convertDateToAgo(feedItem.getPublishTime());

            publisher.setUserId(publisherId);

            switch (feedItem.getType()) {

                case FeedItem.POST:
                    Post post = (Post) feedItem;
                    long views = post.getViews();
                    long upVotes = post.getUpVotes();
                    PostHolder postHolder = (PostHolder) holder;
                    postHolder.wireViews();
                    bindPost(postHolder, itemContent, imagePosted, imageThumb, timeAgo);
                    bindPostUserData(postHolder, publisher, tag);
                    postContentListener(postHolder, itemContent, imagePosted, imageThumb, timeAgo, publisher, feedItemId, comments, views, upVotes);
                    postProfileListener(postHolder, publisher, feedItemId);
                    postOverflowListener(postHolder, publisher, feedItem);

                    break;

                case FeedItem.ANSWER:
                    Answer answer= (Answer) feedItem;
                    long ansViews = answer.getViews();
                    long ansUpvotes = answer.getUpVotes();
                    String quesContent = answer.getQuesContent();
                    AnswerHolder answerHolder = (AnswerHolder) holder;
                    answerHolder.wireViews();
                    bindAnswer(answerHolder, itemContent, quesContent, imagePosted, imageThumb, timeAgo);
                    bindAnswererData(answerHolder, publisher);
                    answerContentListener(answerHolder, itemContent, quesContent, imagePosted, imageThumb,
                            timeAgo, publisher, answer.getQuesId(), answer.getTags(), feedItemId, comments, ansViews, ansUpvotes);
                    answerProfileListener(answerHolder, publisher, feedItemId);
                    answerOverflowListener(answerHolder, publisher, feedItem);

                    break;

                case FeedItem.CRACK:
                    Answer crack= (Answer) feedItem;
                    long crackViews=crack.getViews();
                    long crackUpvotes=crack.getUpVotes();
                    String crackContent = crack.getQuesContent();
                    CrackHolder crackHolder = (CrackHolder) holder;
                    crackHolder.wireViews();
                    bindCrack(crackHolder, itemContent, crackContent, imagePosted, imageThumb, timeAgo);
                    bindCrackerData(crackHolder, publisher);
                    crackContentListener(crackHolder, itemContent, crackContent,imagePosted, imageThumb,
                            timeAgo, publisher, crack.getQuesId(), crack.getTags(), feedItemId, comments, crackViews, crackUpvotes);
                    crackProfileListener(crackHolder, publisher, feedItemId);
                    crackOverflowListener(crackHolder, publisher, feedItem);

                    break;

                case FeedItem.QUESTION:
                    Question question = (Question) feedItem;
                    QuestionHolder questionHolder = (QuestionHolder) holder;
                    questionHolder.wireViews(question.getNoOfAns());
                    bindQuestion(questionHolder, itemContent, timeAgo);
                    questionListener(questionHolder, question);
                    answerBtnListener(questionHolder, question);
                    questionOverflowListener(questionHolder, publisher, feedItem);

                    break;

                case FeedItem.CHALLENGE:
                    Question challenge = (Question) feedItem;
                    ChallengeHolder challengeHolder = (ChallengeHolder) holder;
                    challengeHolder.wireViews(challenge.getNoOfAns());
                    bindChallenge(challengeHolder, itemContent, timeAgo);
                    challengeListener(challengeHolder, challenge);
                    crackBtnListener(challengeHolder, challenge);
                    challengeOverflowListener(challengeHolder, publisher, feedItem);

                    break;
            }

        }
    }

    private void postProfileListener(@NonNull PostHolder holder, final User publisher, final String postId) {

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

    }

    private void postContentListener(@NonNull PostHolder holder, final String itemContent, final String imagePosted, final String imageThumb,
                                     final String timeAgo, final User publisher, final String postId,
                                     final long comments, final long views, final long upvotes) {
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(mContext, itemContent, imagePosted, imageThumb, timeAgo, publisher, postId,
                        comments, views, upvotes);
                mContext.startActivity(intent);

            }
        });
    }

    private void answerContentListener(@NonNull AnswerHolder holder, final String itemContent,
                                       final String questionAsked, final String imagePosted, final String imageThumb,
                                       final String timeAgo, final User publisher, final String questionId, final List<String> tags, final String ansId,
                                       final long comments, final long ansViews, final long ansUpvotes) {
        holder.quesContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(itemContent, questionAsked, imagePosted, imageThumb, timeAgo, publisher,
                        questionId, false, tags.get(0), ansId, comments, ansViews, ansUpvotes);
            }
        });
        holder.answerContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(itemContent, questionAsked, imagePosted, imageThumb, timeAgo, publisher,
                        questionId, false, tags.get(0), ansId, comments, ansViews, ansUpvotes);
            }
        });
    }

    private void answerProfileListener(@NonNull AnswerHolder holder, final User publisher, final String postId) {

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

    }

    private void crackContentListener(@NonNull CrackHolder holder, final String challenge,
                                      final String itemContent, final String imagePosted, final String imageThumb,
                                      final String timeAgo, final User publisher, final String questionId, final List<String> tags, final String crackId,
                                      final long comments, final long crackViews, final long crackUpvotes) {
        holder.challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(challenge, itemContent, imagePosted, imageThumb, timeAgo, publisher, questionId,
                        true, tags.get(0), crackId, comments, crackViews, crackUpvotes);
            }
        });

        holder.crackContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFullAnswerActivity(challenge, itemContent, imagePosted, imageThumb, timeAgo, publisher,questionId,
                        true, tags.get(0), crackId, comments, crackViews, crackUpvotes);
            }
        });
    }

    private void crackProfileListener(@NonNull CrackHolder holder, final User publisher, final String postId) {

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
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

    private void answerBtnListener(@NonNull QuestionHolder holder, final Question question) {
        holder.answer.setOnClickListener(
                answerCrackListener(question, FeedItem.ANSWER));

    }
    private void crackBtnListener(@NonNull ChallengeHolder holder, final Question question) {
        holder.answer.setOnClickListener(
                answerCrackListener(question, FeedItem.CRACK));

    }

    @NonNull
    private View.OnClickListener answerCrackListener(final Question question, final int answerType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AnswerActivity.newIntent(mContext, question);
                intent.putExtra("ANSWER_TYPE", answerType);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        };
    }

    private void questionListener(@NonNull QuestionHolder holder, final Question question) {
        holder.quesContent.setOnClickListener(
                questionChallengeListener(question, FeedItem.ANSWER));
    }
    private void challengeListener(@NonNull ChallengeHolder holder, final Question question) {
        holder.challengeContent.setOnClickListener(
                questionChallengeListener(question, FeedItem.CRACK));
    }

    @NonNull
    private View.OnClickListener questionChallengeListener(final Question question, final int answerType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AllAnswersActivity.newIntent(mContext, question);
                intent.putExtra("ANSWER_TYPE", answerType);
                mContext.startActivity(intent);
               // ((Activity) mContext).overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
            }
        };
    }


    private void bindPostUserData(@NonNull final PostHolder holder, final User user, final String tag) {
        mFireBaseUtil.mFirestore.collection("users").document(user.getUserId()).
                collection("reg_details").document("doc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String name = snapshot.getString("firstName");
                    name=name.concat(" "+snapshot.getString("lastName"));
                        String image = snapshot.getString("dpUrl");

                        holder.setUserData(name, image, tag);

                        user.setDpUrl(image);
                        user.setFirstName(name);

                } else {
                    // Error handling here
                }

            }
        });
    }

    private void bindAnswererData(@NonNull final AnswerHolder holder, final User user) {
        mFireBaseUtil.mFirestore.collection("users").document(user.getUserId()).
                collection("reg_details").document("doc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            String name = snapshot.getString("firstName");
                            name=name.concat(" "+snapshot.getString("lastName"));
                            String image = snapshot.getString("dpUrl");
                            holder.setUserData(name, image);
                            user.setDpUrl(image);
                            user.setFirstName(name);

                        } else {
                            // Error handling here
                        }

                    }
                });
    }

    private void bindCrackerData(@NonNull final CrackHolder holder, final User user) {
        mFireBaseUtil.mFirestore.collection("users").document(user.getUserId()).
                collection("reg_details").document("doc").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            String name = snapshot.getString("firstName");
                            name=name.concat(" "+snapshot.getString("lastName"));
                            String image = snapshot.getString("dpUrl");
                            holder.setUserData(name, image);
                            user.setDpUrl(image);
                            user.setFirstName(name);

                        } else {
                            // Error handling here
                        }

                    }
                });
    }

    private void postOverflowListener(@NonNull PostHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher,OverflowType.POST_ANS_CRACK, feedItem));
    }
    private void answerOverflowListener(@NonNull AnswerHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher,OverflowType.POST_ANS_CRACK, feedItem));
    }
    private void crackOverflowListener(@NonNull CrackHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher, OverflowType.POST_ANS_CRACK, feedItem));
    }

    private void questionOverflowListener(@NonNull QuestionHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher,OverflowType.QUES_CHALLENGE, feedItem));
    }
    private void challengeOverflowListener(@NonNull ChallengeHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher, OverflowType.QUES_CHALLENGE, feedItem));
    }


    private void bindPost(@NonNull PostHolder holder, final String content, final String publishedImg, final String publishedThumb, String timeAgo) {
        holder.setContent(content);
        holder.setPostedImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
    }

    private void bindAnswer(@NonNull AnswerHolder holder, final String answer, final String question, final String publishedImg, final String publishedThumb, String timeAgo) {
        holder.setContent(answer, question);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
    }

    private void bindQuestion(@NonNull QuestionHolder holder, final String content, String timeAgo) {
        holder.setContent(content);
        holder.setTime(timeAgo);
    }

    private void bindChallenge(@NonNull ChallengeHolder holder, final String content, String timeAgo) {
        holder.setContent(content);
        holder.setTime(timeAgo);
    }

    private void bindCrack(@NonNull CrackHolder holder, final String crack, final String challenge, final String publishedImg, final String imageThumb, String timeAgo) {
        holder.setContent(challenge, crack);
        holder.setCrackImgView(publishedImg, imageThumb);
        holder.setTime(timeAgo);
    }

    @Override
    public int getItemCount() {
        return mFeedItems.size();
    }



    // Inner class
    public class PostHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView content;
        private TextView timeAgo;
        private TextView userName;
        private CircleImageView userImage;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private TextView seeMore;

        // PostHolder constructor
        public PostHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void wireViews(){
            overflowImgView = mView.findViewById(R.id.overflow);
            userName = mView.findViewById(R.id.userName);
            userImage = mView.findViewById(R.id.circleImageView);
            timeAgo = mView.findViewById(R.id.answeredTime);
            postedImgView = mView.findViewById(R.id.postedImagee);
            content = mView.findViewById(R.id.answerContent);
            seeMore = mView.findViewById(R.id.seeMore);

        }

        public void setContent(String postedText) {
            content.setText(postedText);
            content.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = content.getLineCount();
                    if(lineCount>=3)
                        seeMore.setVisibility(View.VISIBLE);
                }
            });
        }

        public void setPostedImgView(String imageUrl, String thumbUrl) {
            if(imageUrl != null) {
                postedImgView.setVisibility(View.VISIBLE);
                Glide.with(mContext.getApplicationContext()).load(imageUrl)
                        .thumbnail(Glide.with(mContext.getApplicationContext())
                                .load(thumbUrl)).into(postedImgView);
            }
        }

        public void setTime(String timeAgo) {
            this.timeAgo.setText("posted "+timeAgo);
        }

        public void setUserData(String name, String image, String tag) {
            name=name.concat(" #"+tag);
            userName.setText(name);
            userName.setTypeface(null, Typeface.BOLD);
            userName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=content)
            Glide.with(mContext.getApplicationContext()).load(image).apply(placeholderOption).into(userImage);

        }


    }


    public class AnswerHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView quesContent;
        private TextView answerContent;
        private TextView userName;
        private CircleImageView userImage;
        private TextView timeAgo;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private TextView seeMore;

        public AnswerHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            quesContent = mView.findViewById(R.id.questionTView);
            userImage = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            postedImgView = mView.findViewById(R.id.postedImagee);
            timeAgo = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);

            answerContent = mView.findViewById(R.id.answerContent);
            seeMore = mView.findViewById(R.id.seeMore);
        }

        public void setContent(String answer,String question) {
            quesContent.setText(question);
            answerContent.setText(answer);

            answerContent.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = answerContent.getLineCount();
                    if(lineCount>=3) {
                        seeMore.setVisibility(View.VISIBLE);
                        seeMore.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    }

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
            this.timeAgo.setText("answered "+timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext.getApplicationContext()).load(image).apply(placeholderOption).into(userImage);

        }


    }


    public class CrackHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView challenge;
        private TextView crackContent;
        private TextView userName;
        private CircleImageView userImage;
        private TextView timeAgo;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private ImageView challengeIcon;
        private ImageView crackImgView;
        private TextView seeMore;

        public CrackHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            challenge = mView.findViewById(R.id.questionTView);
            challengeIcon = mView.findViewById(R.id.imageView4);

            userImage = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            postedImgView = mView.findViewById(R.id.postedImagee);

            timeAgo = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);

            crackContent = mView.findViewById(R.id.answerContent);
            crackImgView = mView.findViewById(R.id.postedImagee);
            seeMore = mView.findViewById(R.id.seeMore);
        }

        public void setContent(String question, String answer) {
            challenge.setText(question);
            crackContent.setText(answer);
            crackContent.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = crackContent.getLineCount();
                    if(lineCount>=3) {
                        seeMore.setVisibility(View.VISIBLE);
                        seeMore.setBackgroundColor(Color.parseColor("#f5f5f5"));
                    }

                }
            });
        }
        public void setCrackImgView(String imageUrl, String imageThumb) {
            if(imageUrl != null&&mContext!=null) {
                postedImgView.setVisibility(View.VISIBLE);
                Glide.with(mContext.getApplicationContext()).load(imageUrl)
                        .thumbnail(Glide.with(mContext.getApplicationContext()).load(imageThumb))
                        .into(crackImgView);
            }
        }

        public void setTime(String timeAgo) {
            this.timeAgo.setText("cracked "+timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext.getApplicationContext()).load(image).apply(placeholderOption).into(userImage);

        }


    }



    public class QuestionHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView quesContent;
        private TextView timeAgo;
        private TextView noCracksYet;

        private TextView answer;
        private TextView followQues;
        private TextView pass;
        private ImageView overflowImgView;

        public QuestionHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(int noOfAns){
            quesContent = mView.findViewById(R.id.questionTView);
            timeAgo = mView.findViewById(R.id.postedTime);
            noCracksYet = mView.findViewById(R.id.noAnswerYet);

            if(noOfAns==1)
                noCracksYet.setText(noOfAns+ " Answer.");
            else if(noOfAns>1)
                noCracksYet.setText(noOfAns+ " Answers.");

            answer=mView.findViewById(R.id.answer);
            pass=mView.findViewById(R.id.pass);
            followQues=mView.findViewById(R.id.followQues);
            overflowImgView = mView.findViewById(R.id.quesOverFlow);
        }

        public void setContent(String question) {
            if(question!=null)
            quesContent.setText(question);
        }


        public void setTime(String timeAgo) {
            this.timeAgo.setText("asked "+timeAgo);
        }



    }


    public class ChallengeHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView challengeContent;
        private TextView timeAgo;
        private TextView noCracksYet;
        private ImageView challengeIcon;
        private TextView answer;

        private TextView followQues;
        private TextView pass;
        private ImageView overflowImgView;

        public ChallengeHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(int noOfCracks){
            noCracksYet = mView.findViewById(R.id.noAnswerYet);
            if(noOfCracks==0)
                noCracksYet.setText("No Cracks Yet.");
            else if(noOfCracks==1)
                noCracksYet.setText(noOfCracks+ " Crack.");
            else
                noCracksYet.setText(noOfCracks+ " Cracks.");

            challengeContent = mView.findViewById(R.id.questionTView);
            timeAgo = mView.findViewById(R.id.postedTime);
            challengeIcon = mView.findViewById(R.id.imageView2);
            challengeIcon.setVisibility(View.VISIBLE);

            answer=mView.findViewById(R.id.answer);
            answer.setText("Crack");
            pass=mView.findViewById(R.id.pass);
            followQues=mView.findViewById(R.id.followQues);
            overflowImgView = mView.findViewById(R.id.quesOverFlow);
        }

        public void setContent(String challenge) {
            this.challengeContent.setText(challenge);
        }


        public void setTime(String timeAgo) {
            this.timeAgo.setText("challenged "+timeAgo);
        }


    }

}
