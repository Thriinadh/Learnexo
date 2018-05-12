package com.learnexo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.main.FullPostActivity;
import com.learnexo.main.QuestionAnswerActivity;
import com.learnexo.main.R;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    BottomSheetDialog mDialog;
    private List<FeedItem> mFeedItems;
    private Context mContext;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

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
                return FeedItem.NO_ANS_QUES;
            case 4:
                return FeedItem.NO_CRACK_CHALLENGE;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crack_list_item, parent, false);//change this
                return new CrackHolder(view);

            case FeedItem.NO_ANS_QUES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_ans_ques_item, parent, false);//change this
                return new QuestionHolder(view);

            case FeedItem.NO_CRACK_CHALLENGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_crack_challenge_item, parent, false);//change this
                return new ChallengeHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        FeedItem feedItem= mFeedItems.get(position);


        if(feedItem!=null) {

            User publisher = new User();
            final String publisherId = feedItem.getUserId();
            final String itemContent = feedItem.getContent();
            final String imagePosted = feedItem.getImgUrl();
            final String timeAgo = convertDateToAgo(feedItem.getPublishTime());
            publisher.setUserId(publisherId);

            switch (feedItem.getType()) {
                case FeedItem.POST:
                    PostHolder postHolder = (PostHolder) holder;

                    bindPost(postHolder, itemContent, imagePosted, timeAgo);
                    bindPostUserData(postHolder, publisher);

                    postContentListener(postHolder, itemContent, imagePosted, timeAgo);
                    postOverflowListener(postHolder, publisher, feedItem);

                    break;

                case FeedItem.ANSWER:
                    Answer answer= (Answer) feedItem;
                    AnswerHolder answerHolder = (AnswerHolder) holder;
                    bindAnswer(answerHolder, itemContent, answer.getQuesName(), imagePosted, timeAgo);
                    bindAnswererData(answerHolder, publisher);
                    answerContentListener(answerHolder, itemContent, imagePosted, timeAgo);
//                    questionOverflowListener(answerHolder, publisher, feedItem);

                    break;

                case FeedItem.CRACK:
                    Answer crack= (Answer) feedItem;
                    CrackHolder crackHolder = (CrackHolder) holder;
                    bindCrack(crackHolder, itemContent, crack.getQuesName(), imagePosted, timeAgo);
                    bindCrackerData(crackHolder, publisher);
                    crackContentListener(crackHolder, itemContent, imagePosted, timeAgo);
//                    questionOverflowListener(quesViewHolder, publisher, feedItem);
//
                    break;

                case FeedItem.NO_ANS_QUES:
                    Question question = (Question) feedItem;
                    QuestionHolder quesViewHolder = (QuestionHolder) holder;
                    quesViewHolder.wireViews();
                    bindQuestion(quesViewHolder, itemContent);
                    noAnswerAnsListener(quesViewHolder, itemContent);

                    break;

                case FeedItem.NO_CRACK_CHALLENGE:
                    ChallengeHolder challengeHolder = (ChallengeHolder) holder;
                    challengeHolder.wireViews();
                    bindChallenge(challengeHolder, itemContent, timeAgo);
//
                    break;
            }

        }
    }


    private void postContentListener(@NonNull PostHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(mContext, itemContent, imagePosted, timeAgo);
                mContext.startActivity(intent);
            }
        });
    }

    private void answerContentListener(@NonNull AnswerHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.quesContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(mContext, itemContent, imagePosted, timeAgo);
                mContext.startActivity(intent);
            }
        });
    }

    private void crackContentListener(@NonNull CrackHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.crackContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(mContext, itemContent, imagePosted, timeAgo);
                mContext.startActivity(intent);
            }
        });
    }

    private void noAnswerAnsListener(@NonNull QuestionHolder holder, final String itemContent) {

        holder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = QuestionAnswerActivity.newIntent(mContext, itemContent);
                mContext.startActivity(intent);
                ((Activity) mContext).overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

    }

    private void bindPostUserData(@NonNull final PostHolder holder, final User user) {
        mFirebaseUtil.mFirestore.collection("users").document(user.getUserId()).
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

    private void bindAnswererData(@NonNull final AnswerHolder holder, final User user) {
        mFirebaseUtil.mFirestore.collection("users").document(user.getUserId()).
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
        mFirebaseUtil.mFirestore.collection("users").document(user.getUserId()).
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
        holder.setOverflowImgView();
        holder.overflowImgView.setOnClickListener(new View.OnClickListener() {

            private TextView followTView;
            private LinearLayout followBtnLayout;
            private LinearLayout deleteBtnLayout;
            private LinearLayout editBtnLayout;
            private LinearLayout copyBtnLayout;
            private LinearLayout notifBtnLayout;
            private LinearLayout connectBtnLayout;

            @Override
            public void onClick(View view) {

                View bottomSheetView = View.inflate(mContext, R.layout.bottom_sheet_dialog_for_sharedposts, null);


                mDialog = new BottomSheetDialog(mContext);
                mDialog.setContentView(bottomSheetView);
                mDialog.show();

                inflateBottomSheetButtons(bottomSheetView);
            }

            private void inflateBottomSheetButtons(final View bottomSheetView) {

                followTView = bottomSheetView.findViewById(R.id.followTView);
                followBtnLayout = bottomSheetView.findViewById(R.id.followUser);
                deleteBtnLayout = bottomSheetView.findViewById(R.id.deleteBtn);
                editBtnLayout = bottomSheetView.findViewById(R.id.editNameBtn);
                copyBtnLayout = bottomSheetView.findViewById(R.id.copyBtn);
                notifBtnLayout =  bottomSheetView.findViewById(R.id.notifBtn);
                connectBtnLayout =  bottomSheetView.findViewById(R.id.connectBtn);

                followBtnLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();

                        final Map<String, Object> followingUser = new HashMap<>();
                        followingUser.put("name", publisher.getFirstName());
                        followingUser.put("dpUrl", publisher.getDpUrl());

                       mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
                               .collection("following").document(publisher.getUserId()).set(followingUser)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               followingUser.put("name", FeedFragment.sName);
                               followingUser.put("dpUrl", FeedFragment.sDpUrl);
                               mFirebaseUtil.mFirestore.collection("users").document(publisher.getUserId())
                                       .collection("followers").document(FirebaseUtil.getCurrentUserId()).set(followingUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {

                                       followTView.setText("Unfollow");

                                       Toast.makeText(mContext, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_LONG).show();

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();
                                   }
                               });


                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                               Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();

                               Log.d("FeedAdapter", "SomethingWentWrong "+e);

                           }
                       });

                    }
                });

            }
        });
    }

    private void questionOverflowListener(@NonNull AnswerHolder holder, final User publisher, final FeedItem feedItem) {
        holder.overflowImgView.setOnClickListener(new View.OnClickListener() {

            private TextView followTView;
            private LinearLayout followBtnLayout;
            private LinearLayout deleteBtnLayout;
            private LinearLayout editBtnLayout;
            private LinearLayout copyBtnLayout;
            private LinearLayout notifBtnLayout;
            private LinearLayout connectBtnLayout;

            @Override
            public void onClick(View view) {

                View bottomSheetView = View.inflate(mContext, R.layout.bottom_sheet_dialog_for_sharedposts, null);


                mDialog = new BottomSheetDialog(mContext);
                mDialog.setContentView(bottomSheetView);
                mDialog.show();

                inflateBottomSheetButtons(bottomSheetView);
            }

            private void inflateBottomSheetButtons(final View bottomSheetView) {

                followTView = bottomSheetView.findViewById(R.id.followTView);
                followBtnLayout = bottomSheetView.findViewById(R.id.followUser);
                deleteBtnLayout = bottomSheetView.findViewById(R.id.deleteBtn);
                editBtnLayout = bottomSheetView.findViewById(R.id.editNameBtn);
                copyBtnLayout = bottomSheetView.findViewById(R.id.copyBtn);
                notifBtnLayout =  bottomSheetView.findViewById(R.id.notifBtn);
                connectBtnLayout =  bottomSheetView.findViewById(R.id.connectBtn);

                followBtnLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();

                        final Map<String, Object> followingUser = new HashMap<>();
                        followingUser.put("name", publisher.getFirstName());
                        followingUser.put("dpUrl", publisher.getDpUrl());

                        mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
                                .collection("following").document(publisher.getUserId()).set(followingUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        followingUser.put("name", FeedFragment.sName);
                                        followingUser.put("dpUrl", FeedFragment.sDpUrl);
                                        mFirebaseUtil.mFirestore.collection("users").document(publisher.getUserId())
                                                .collection("followers").document(FirebaseUtil.getCurrentUserId()).set(followingUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                followTView.setText("Unfollow");

                                                Toast.makeText(mContext, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();

                                Log.d("FeedAdapter", "SomethingWentWrong "+e);

                            }
                        });

                    }
                });

            }
        });
    }

    private void bindPost(@NonNull PostHolder holder, final String content, final String publishedImg, String timeAgo) {
        holder.setContent(content);
        holder.setPostedImgView(publishedImg);
        holder.setTime(timeAgo);
    }

    private void bindAnswer(@NonNull AnswerHolder holder, final String content, final String answer, final String publishedImg, String timeAgo) {
        holder.setContent(content, answer);
        holder.setAnsImgView(publishedImg);
        holder.setTime(timeAgo);
    }

    private void bindQuestion(@NonNull QuestionHolder holder, final String content) {
        holder.setContent(content);
       // holder.setTime(timeAgo);
    }

    private void bindChallenge(@NonNull ChallengeHolder holder, final String content, String timeAgo) {
        holder.setContent(content);
        holder.setTime(timeAgo);
    }

    private void bindCrack(@NonNull CrackHolder holder, final String content, final String answer, final String publishedImg, String timeAgo) {
        holder.setContent(content, answer);
        holder.setCrackImgView(publishedImg);
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

        // PostHolder constructor
        public PostHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String postedText) {
            content = mView.findViewById(R.id.feed_content);
            content.setText(postedText);
        }

        public void setPostedImgView(String imageUrl) {
            // if(imageUrl != null)
            // crackImgView.setImageURI(Uri.parse(imageUrl));

            postedImgView = mView.findViewById(R.id.postedImagee);
            if(imageUrl != null)
            Glide.with(mContext).load(imageUrl).into(postedImgView);
        }

        public void setTime(String timeAgo) {
            this.timeAgo = mView.findViewById(R.id.feed_date);
            this.timeAgo.setText(timeAgo);
        }

        public void setUserData(String name, String image) {
            userName = mView.findViewById(R.id.userNameTView);
            userName.setText(name);

            userImage = mView.findViewById(R.id.feed_user_image);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=content)
            Glide.with(mContext).load(image).apply(placeholderOption).into(userImage);

        }

        public void setOverflowImgView() {
            overflowImgView = mView.findViewById(R.id.overflow);
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
        private ImageView ansImgView;

        public AnswerHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            quesContent = mView.findViewById(R.id.questionTView);
            userImage = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);

            timeAgo = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);

            answerContent = mView.findViewById(R.id.answerContent);
            ansImgView = mView.findViewById(R.id.postedImagee);
        }

        public void setContent(String question, String answer) {
            quesContent.setText(question);
            answerContent.setText(answer);
        }
        public void setAnsImgView(String imageUrl) {
            if(imageUrl != null&&mContext!=null)
                Glide.with(mContext).load(imageUrl).into(ansImgView);
        }

        public void setTime(String timeAgo) {
            this.timeAgo.setText(timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext).load(image).apply(placeholderOption).into(userImage);

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
        private ImageView challengeIcon;
        private ImageView crackImgView;

        public CrackHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            challenge = mView.findViewById(R.id.questionTView);
            challengeIcon = mView.findViewById(R.id.imageView4);

            userImage = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);

            timeAgo = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);

            crackContent = mView.findViewById(R.id.answerContent);
            crackImgView = mView.findViewById(R.id.postedImagee);
        }

        public void setContent(String question, String answer) {
            challenge.setText(question);
            crackContent.setText(answer);
        }
        public void setCrackImgView(String imageUrl) {
            if(imageUrl != null&&mContext!=null)
                Glide.with(mContext).load(imageUrl).into(crackImgView);
        }

        public void setTime(String timeAgo) {
            this.timeAgo.setText(timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext).load(image).apply(placeholderOption).into(userImage);

        }


    }



    public class QuestionHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView quesContent;
        private TextView timeAgo;


        private TextView answer;
        private TextView followQues;
        private TextView pass;
        private ImageView overflowImgView;


        public QuestionHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            quesContent = mView.findViewById(R.id.questionTView);
            timeAgo = mView.findViewById(R.id.postedTime);

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
            this.timeAgo.setText(timeAgo);
        }



    }


    public class ChallengeHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView challenge;
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

        public void wireViews(){
            noCracksYet = mView.findViewById(R.id.noAnswerYet);
            noCracksYet.setText("No Cracks Yet");
            challenge = mView.findViewById(R.id.questionTView);
            timeAgo = mView.findViewById(R.id.postedTime);
            challengeIcon = mView.findViewById(R.id.imageView2);

            answer=mView.findViewById(R.id.answer);
            answer.setText("Crack");
            pass=mView.findViewById(R.id.pass);
            followQues=mView.findViewById(R.id.followQues);
            overflowImgView = mView.findViewById(R.id.quesOverFlow);
        }

        public void setContent(String challenge) {
            this.challenge.setText(challenge);
        }


        public void setTime(String timeAgo) {
            this.timeAgo.setText(timeAgo);
        }


    }




}
