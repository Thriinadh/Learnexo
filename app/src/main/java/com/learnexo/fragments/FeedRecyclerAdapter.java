package com.learnexo.fragments;

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
import com.learnexo.main.R;
import com.learnexo.model.feed.FeedItem;
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
    private Context context;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public FeedRecyclerAdapter(List<FeedItem> mFeedItems) {
        this.mFeedItems = mFeedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;

        switch (viewType) {
            case FeedItem.POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
                return new PostHolder(view);

            case FeedItem.QUESTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_with_no_ans, parent, false);
                return new QuesViewHolder(view);

            case FeedItem.CHALLENGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_with_no_ans, parent, false);
                return new QuesViewHolder(view);
            case FeedItem.NO_ANS_QUES:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_with_no_ans, parent, false);
                return new QuesViewHolder(view);

            case FeedItem.NO_ANS_CHALLENGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_with_no_ans, parent, false);
                return new QuesViewHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        FeedItem feedItem=mFeedItems.get(position);
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

                case FeedItem.QUESTION:
                    QuesViewHolder quesViewHolder = (QuesViewHolder) holder;

                    bindQuestion(quesViewHolder, itemContent, timeAgo);
                    bindQuestionUserData(quesViewHolder, publisher);

                    questionContentListener(quesViewHolder, itemContent, imagePosted, timeAgo);
                    questionOverflowListener(quesViewHolder, publisher, feedItem);

                    break;

                case FeedItem.CHALLENGE:
//                    QuesViewHolder quesViewHolder = (QuesViewHolder) holder;
//
//                    bindQuestion(quesViewHolder, itemContent, timeAgo);
//                    bindQuestionUserData(quesViewHolder, publisher);
//
//                    questionContentListener(quesViewHolder, itemContent, imagePosted, timeAgo);
//                    questionOverflowListener(quesViewHolder, publisher, feedItem);
//
//                    break;
                case FeedItem.NO_ANS_QUES:
//                    QuesViewHolder quesViewHolder = (QuesViewHolder) holder;
//
//                    bindQuestion(quesViewHolder, itemContent, timeAgo);
//                    bindQuestionUserData(quesViewHolder, publisher);
//
//                    questionContentListener(quesViewHolder, itemContent, imagePosted, timeAgo);
//                    questionOverflowListener(quesViewHolder, publisher, feedItem);
//
//                    break;
                case FeedItem.NO_ANS_CHALLENGE:
//                    QuesViewHolder quesViewHolder = (QuesViewHolder) holder;
//
//                    bindQuestion(quesViewHolder, itemContent, timeAgo);
//                    bindQuestionUserData(quesViewHolder, publisher);
//
//                    questionContentListener(quesViewHolder, itemContent, imagePosted, timeAgo);
//                    questionOverflowListener(quesViewHolder, publisher, feedItem);
//
//                    break;
            }

        }
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
                return FeedItem.QUESTION;
            case 2:
                return FeedItem.CHALLENGE;
            case 3:
                return FeedItem.NO_ANS_QUES;
            case 4:
                return FeedItem.NO_ANS_CHALLENGE;
            default:
                return -1;
        }
    }

    private void postContentListener(@NonNull PostHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(context, itemContent, imagePosted, timeAgo);
                context.startActivity(intent);
            }
        });
    }

    private void questionContentListener(@NonNull QuesViewHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.quesContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(context, itemContent, imagePosted, timeAgo);
                context.startActivity(intent);
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

    private void bindQuestionUserData(@NonNull final QuesViewHolder holder, final User user) {
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

                View bottomSheetView = View.inflate(context, R.layout.bottom_sheet_dialog_for_sharedposts, null);


                mDialog = new BottomSheetDialog(context);
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

                                       Toast.makeText(context, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_LONG).show();

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(context, "SomethingWentWrong", Toast.LENGTH_LONG).show();
                                   }
                               });


                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                               Toast.makeText(context, "SomethingWentWrong", Toast.LENGTH_LONG).show();

                               Log.d("FeedAdapter", "SomethingWentWrong "+e);

                           }
                       });

                    }
                });

            }
        });
    }

    private void questionOverflowListener(@NonNull QuesViewHolder holder, final User publisher, final FeedItem feedItem) {
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

                View bottomSheetView = View.inflate(context, R.layout.bottom_sheet_dialog_for_sharedposts, null);


                mDialog = new BottomSheetDialog(context);
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

                                                Toast.makeText(context, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "SomethingWentWrong", Toast.LENGTH_LONG).show();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context, "SomethingWentWrong", Toast.LENGTH_LONG).show();

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

    private void bindQuestion(@NonNull QuesViewHolder holder, final String content, String timeAgo) {
        holder.setContent(content);
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
            // postedImgView.setImageURI(Uri.parse(imageUrl));

            postedImgView = mView.findViewById(R.id.postedImagee);
            if(imageUrl != null)
            Glide.with(context).load(imageUrl).into(postedImgView);
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
            Glide.with(context).load(image).apply(placeholderOption).into(userImage);

        }

        public void setOverflowImgView() {
            overflowImgView = mView.findViewById(R.id.overflow);
        }

    }

    public class QuesViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView quesContent;
        private TextView userName;
        private CircleImageView userImage;
        private TextView timeAgo;
        private ImageView overflowImgView;
        private ImageView postedImgView;

        public QuesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String postedQues) {
            quesContent = mView.findViewById(R.id.questionTView);
            quesContent.setText(postedQues);
        }

        public void setTime(String timeAgo) {
            this.timeAgo = mView.findViewById(R.id.postedTime);
            this.timeAgo.setText(timeAgo);
        }

        public void setOverflowImgView() {
            overflowImgView = mView.findViewById(R.id.quesOverFlow);
        }


    }



}
