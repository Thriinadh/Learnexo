package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.learnexo.fragments.FeedFragment;
import com.learnexo.fragments.OverflowMenuListener;
import com.learnexo.model.core.BookMarkType;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.likediv.Bookmark;
import com.learnexo.model.feed.likediv.Comment;
import com.learnexo.model.feed.question.Question;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullAnswerActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_CONTENT = "com.learnexo.questiondata";
    public static final String EXTRA_CONTENT = "com.learnexo.answerdata";
    public static final String EXTRA_PUBLISHER_NAME = "com.learnexo.publisher_name";
    public static final String EXTRA_PUBLISHER_DP = "com.learnexo.publisher_dp";
    private static final String EXTRA_IMAGE = "com.learnexo.imageposted";
    private static final String EXTRA_THUMBNAIL = "com.learnexo.imagepostedthumbnail";
    private static final String EXTRA_TIME = "com.learnexo.postedtime";
    private static final String EXTRA_QUESTION_ID = "com.learnexo.questionId";
    private String mUserId=FirebaseUtil.getCurrentUserId();

    private List<Comment> mComments;
    private CommentsAdapter mAdapter;

    private TextView viewAllAns;
    private TextView questionAsked;
    private TextView fullText;
    private TextView timeOfPost;
    private TextView userName;
    private TextView viewsText;
    private TextView likesCount;
    private TextView commentBtn;
    private TextView seeAllComments;
    private ImageView overFlowBtn;
    private ImageView postedImage;
    private ImageView challengeIcon;
    private ImageView LikeBtn;
    private CircleImageView profileImage;
    private CircleImageView commentsImage;
    private RecyclerView commentsRecycler;

    private String questionId;
    private String questionData;
    private String tag;
    private String questionerId;
    private String ansPublisherId;
    private String answerId;
    private String posTime;
    private String postData;
    private String imagePosted;
    private String imageThumb;
    private String publisherName;
    private String publisherDP;
    private ImageView full_answer_bookmark;
    private String bookMarkItemIdd;

    private long comments;
    private long upVotes;
    private long views;
    private boolean is_crack;
    private boolean flag = true;
    private boolean gag = true;

    private Answer mAnswer;

    FirebaseUtil mFirebaseUtil=new FirebaseUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_answer);
        hangleIntent();

        wireViews();
        setupCommentsRecycler();

        bindData(posTime, postData, imagePosted, imageThumb);

        bindViewsUpvotes();
        commentBtnListener();
        seeAllCommentsListener();

        viewAllAnswersListener();

        final User publisher =new User(ansPublisherId,publisherName,publisherDP);
        othersProfileListeners(publisher);
        overflowListener(publisher);

        checkIfAlreadyBookMarked();
        bookMarkBtnListener();

    }

    private void overflowListener(User publisher) {
        overFlowBtn.setOnClickListener(new OverflowMenuListener(this, publisher, OverflowType.POST_ANS_CRACK,mAnswer));
    }

    private void viewAllAnswersListener() {
        viewAllAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question question=new Question();
                question.setFeedItemId(questionId);
                question.setContent(questionData);
                question.setUserId(questionerId);
                question.setTags(Collections.singletonList(tag));
                Intent intent11 = AllAnswersActivity.newIntent(FullAnswerActivity.this, question);
                if(is_crack)
                    intent11.putExtra("ANSWER_TYPE", FeedItem.CRACK);
                else
                    intent11.putExtra("ANSWER_TYPE", FeedItem.ANSWER);
                startActivity(intent11);

            }
        });
    }

    private void checkIfAlreadyBookMarked() {
        mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                .collection("bookmarks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot documentSnapshot : documents) {
                    Object bookMarkItemId = documentSnapshot.get("bookMarkItemId");
                    bookMarkItemIdd = (String) bookMarkItemId;
                    if(bookMarkItemIdd != null) {
                        if (bookMarkItemIdd.equals(answerId)) {
                            Drawable drawable = ContextCompat.getDrawable(FullAnswerActivity.this, R.drawable.ic_baseline_bookmark_24px);
                            full_answer_bookmark.setImageDrawable(drawable);
                            if(drawable != null)
                                drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                            flag = false;
                            gag = false;
                        }

                    }
                }


            }
        });
    }

    private void bookMarkBtnListener() {
        full_answer_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount(0);
                anim.setDuration(300);

                full_answer_bookmark.startAnimation(anim);

                if(flag) {
                    Drawable drawable = ContextCompat.getDrawable(FullAnswerActivity.this, R.drawable.ic_baseline_bookmark_24px);
                    full_answer_bookmark.setImageDrawable(drawable);
                    if(drawable != null)
                        drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                    flag = false;
                } else {
                    Drawable drawable = ContextCompat.getDrawable(FullAnswerActivity.this, R.drawable.ic_outline_bookmark_border_24px);
                    full_answer_bookmark.setImageDrawable(drawable);
                    if(drawable != null)
                        drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                    flag = true;
                }

            }
        });
    }

    private void commentBtnListener() {
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullAnswerActivity.this, CommentsActivity.class);
                intent.putExtra("EXTRA_QUESTION_ID", questionId);
                intent.putExtra("EXTRA_FEED_ITEM_ID", answerId);
                intent.putExtra("EXTRA_PUBLISHER_IDDD", ansPublisherId);
                intent.putExtra("IF_FROM_FULL_ANSWER_ACTIVITY", flag);
                startActivity(intent);
            }
        });
        commentsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullAnswerActivity.this, CommentsActivity.class);
                intent.putExtra("EXTRA_QUESTION_ID", questionId);
                intent.putExtra("EXTRA_FEED_ITEM_ID", answerId);
                intent.putExtra("EXTRA_PUBLISHER_IDDD", ansPublisherId);
                intent.putExtra("IF_FROM_FULL_ANSWER_ACTIVITY", flag);
                startActivity(intent);
            }
        });
    }

    private void othersProfileListeners(final User publisher) {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(FullAnswerActivity.this, publisher);
                startActivity(intent);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(FullAnswerActivity.this, publisher);
                startActivity(intent);
            }
        });
    }

    @NonNull
    private void hangleIntent() {
        Intent intent=getIntent();
        is_crack = intent.getBooleanExtra("IS_CRACK", false);
        ansPublisherId = intent.getStringExtra("ANS_PUBLISHER_ID");
        answerId = intent.getStringExtra("ANS_ID");
        tag = intent.getStringExtra("TAG");
        questionerId = intent.getStringExtra("QUESTIONER_ID");
        questionData = intent.getStringExtra(EXTRA_QUESTION_CONTENT);
        questionId = intent.getStringExtra(EXTRA_QUESTION_ID);
        posTime = intent.getStringExtra(EXTRA_TIME);
        postData = intent.getStringExtra(EXTRA_CONTENT);
        imagePosted = intent.getStringExtra(EXTRA_IMAGE);
        imageThumb = intent.getStringExtra(EXTRA_THUMBNAIL);
        publisherName = intent.getStringExtra(EXTRA_PUBLISHER_NAME);
        publisherDP = intent.getStringExtra(EXTRA_PUBLISHER_DP);
        comments=intent.getLongExtra("ANS_COMMENTS",0);
        views=intent.getLongExtra("ANS_VIEWS",0);
        upVotes=intent.getLongExtra("ANS_UPVOTES",0);

        mAnswer=new Answer();
        mAnswer.setFeedItemId(answerId);
        if(is_crack)
            mAnswer.setType(FeedItem.CRACK);
        else
            mAnswer.setType(FeedItem.ANSWER);

        mAnswer.setUserId(ansPublisherId);
        mAnswer.setUserName(publisherName);
    }

    public void bindViewsUpvotes() {
        try {
            //store it in his activity log
            //generate edge rank
            //notify publisher
            //notify his followers

            //upVoteListener();

            bindViews();
            new ViewChecker().execute();
            new UpVoteChecker().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upVoteListener(Boolean isUpVoted) {
        LikeBtn.setOnClickListener(
                new UpVoteListener(LikeBtn,likesCount,flag, ansPublisherId, answerId, upVotes,FullAnswerActivity.this, true, null, is_crack, isUpVoted)
        );
    }

    private void incrementViews() {
        long viewss = views+1;
        Map<String, Object> map= new HashMap();
        map.put("views",viewss);

        mFirebaseUtil.mFirestore.collection("users").document(ansPublisherId).collection("answers").document(answerId).update(map);
        mFirebaseUtil.mFirestore.collection("questions").document(questionId).collection("answers").document(answerId).update(map);

        Map<String, Object> map1= new HashMap();
        map1.put("publisherId",ansPublisherId);
        if(is_crack)
            map1.put("feedItemType", FeedItem.CRACK);
        else
            map1.put("feedItemType", FeedItem.ANSWER);

        mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("views").document(answerId).set(map1);
    }

    private void bindViews() {
        likesCount.setText(upVotes+" Up votes");
        if(views==0){
            views=1;
            viewsText.setText("1 View");
        }else{
            viewsText.setText(views+ " Views");
        }
    }

    private void bindData(String posTime, String postData, String imagePosted, String imageThumb) {
        questionAsked.setText(questionData);
        fullText.setText(postData);
        userName.setText(publisherName);
        timeOfPost.setText(posTime);
        if(is_crack){
            challengeIcon = findViewById(R.id.imageView5);
            challengeIcon.setVisibility(View.VISIBLE);
            viewAllAns.setText("View all cracks");
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(publisherDP).apply(requestOptions).into(profileImage);


        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(imagePosted)
                .thumbnail(Glide.with(getApplicationContext()).load(imageThumb))
                .apply(requestOptions).into(postedImage);
    }

    private void wireViews() {
        viewsText = findViewById(R.id.viewsText);
        likesCount = findViewById(R.id.likesCount);
        LikeBtn = findViewById(R.id.full_post_like);
        viewAllAns = findViewById(R.id.viewAllAns);
        questionAsked = findViewById(R.id.questionAsked);
        fullText = findViewById(R.id.full_text);
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.userNameTView);
        timeOfPost = findViewById(R.id.feed_time);
        postedImage = findViewById(R.id.postedImage);
        overFlowBtn = findViewById(R.id.imageView);
        full_answer_bookmark = findViewById(R.id.full_answer_bookmark);

        commentsImage = findViewById(R.id.commentsImage);
        commentBtn = findViewById(R.id.commentBtn);
        seeAllComments = findViewById(R.id.seeAllComments);
        if(comments==0)
            seeAllComments.setVisibility(View.INVISIBLE);
        commentsRecycler = findViewById(R.id.commentsRecycler);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getApplicationContext()).load(FeedFragment.sDpUrl).apply(requestOptions).into(commentsImage);
    }

    public static Intent newIntent(Context context, String questionAsked, String content, String publishedImg, String imageThumb,
                                   String timeAgo, User publisher, String questionId, String tag, String ansId,
                                   long comments, long ansViews, long ansUpvotes) {

        Intent intent = new Intent(context, FullAnswerActivity.class);
        intent.putExtra(EXTRA_QUESTION_CONTENT, questionAsked);
        intent.putExtra(EXTRA_CONTENT, content);
        intent.putExtra(EXTRA_TIME, timeAgo);
        intent.putExtra(EXTRA_PUBLISHER_NAME, publisher.getFirstName());
        intent.putExtra(EXTRA_PUBLISHER_DP, publisher.getDpUrl());
        intent.putExtra(EXTRA_QUESTION_ID, questionId);
        intent.putExtra("TAG", tag);
        intent.putExtra("QUESTIONER_ID", publisher.getUserId());
        intent.putExtra("ANS_ID", ansId);
        intent.putExtra("ANS_PUBLISHER_ID",publisher.getUserId());
        intent.putExtra("ANS_COMMENTS",comments);
        intent.putExtra("ANS_VIEWS",ansViews);
        intent.putExtra("ANS_UPVOTES",ansUpvotes);
        if(publishedImg!=null) {
            intent.putExtra(EXTRA_IMAGE, publishedImg);
            intent.putExtra(EXTRA_THUMBNAIL, imageThumb);
        }
        return intent;
    }


    private void setupCommentsRecycler() {
        mComments = new ArrayList<>();
        mAdapter = new CommentsAdapter(mComments);

        commentsRecycler = findViewById(R.id.commentsRecycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(FullAnswerActivity.this));
        commentsRecycler.setAdapter(mAdapter);
    }

    private void seeAllCommentsListener() {
        seeAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seeAllComments.setEnabled(false);

                mFirebaseUtil.mFirestore.collection("questions").document(questionId).collection("answers").
                        document(answerId).collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: documents) {
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            mComments.add(comment);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }


    private void insertIncrementBookMark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setBookMarkItemId(answerId);
        bookmark.setBookMarkType(BookMarkType.ANSWER);
        bookmark.setPublisherId(ansPublisherId);

        mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                .collection("bookmarks").add(bookmark).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mFirebaseUtil.mFirestore.collection("users").document(ansPublisherId)
                        .collection("answers").document(answerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                        noOfBookMarks = noOfBookMarks + 1;

                        Map<String, Object> map = new HashMap<>();
                        map.put("bookMarks", noOfBookMarks);

                        mFirebaseUtil.mFirestore.collection("users").document(ansPublisherId)
                                .collection("answers").document(answerId).update(map);

                        mFirebaseUtil.mFirestore.collection("questions").document(questionId)
                                .collection("answers").document(answerId).update(map);

                    }
                });
            }
        });
    }

    private void deleteDecrementBookMark() {
        CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users")
                .document(mUserId).collection("bookmarks");
        Query query = collectionReference.whereEqualTo("bookMarkItemId", answerId);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                DocumentSnapshot documentSnapshot = documents.get(0);
                String id = documentSnapshot.getId();

                mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                        .collection("bookmarks").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mFirebaseUtil.mFirestore.collection("users").document(ansPublisherId)
                                .collection("answers").document(answerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                                noOfBookMarks = noOfBookMarks - 1;

                                Map<String, Object> map = new HashMap<>();
                                map.put("bookMarks", noOfBookMarks);

                                mFirebaseUtil.mFirestore.collection("users").document(ansPublisherId)
                                        .collection("answers").document(answerId).update(map);

                                mFirebaseUtil.mFirestore.collection("questions").document(questionId)
                                        .collection("answers").document(answerId).update(map);

                            }
                        });

                    }
                });

            }
        });
    }

    public class ViewChecker extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("views").
                    document(answerId).get();
            DocumentSnapshot documentSnapshot=null;
            try {

                documentSnapshot = Tasks.await(documentSnapshotTask);


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return (documentSnapshot!=null&&documentSnapshot.exists());


        }

        @Override
        protected void onPostExecute(Boolean isViewed) {
            super.onPostExecute(isViewed);
            if(!isViewed)
                incrementViews();
        }
    }


    public class UpVoteChecker extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {

            Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("up_votes").
                    document(answerId).get();
            DocumentSnapshot documentSnapshot=null;
            try {

                documentSnapshot = Tasks.await(documentSnapshotTask);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return (documentSnapshot!=null&&documentSnapshot.exists());


        }

        @Override
        protected void onPostExecute(Boolean isUpVoted) {
            super.onPostExecute(isUpVoted);

            upVoteListener(isUpVoted);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!flag && gag) {
            insertIncrementBookMark();
        } else if(flag && !gag) {
            deleteDecrementBookMark();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        seeAllComments.setEnabled(true);
    }


}
