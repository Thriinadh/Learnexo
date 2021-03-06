package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.learnexo.model.feed.likediv.UpVote;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class AllAnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Answer> mAnswers;

    private Context mContext;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    private Set<Bookmark> addBookMarks =new HashSet<>();
    private Set<Bookmark> removeBookMarks =new HashSet<>();
    private Set<String> prevBkMarkedAnsIds =new HashSet<>();

    private Set<UpVote> addUpVotes =new HashSet<>();
    private Set<UpVote> removeUpVotes =new HashSet<>();
    private Set<String> prevUpVotedAnsIds =new HashSet<>();

    private String quesId;
    private String mUserId=FirebaseUtil.getCurrentUserId();

    private RotateAnimation anim;

    private Drawable baseline;
    private Drawable outline;
    private Drawable bookMarkBaseline;
    private Drawable bookMarkOutline;


    public AllAnswersAdapter(List<Answer> mFeedItems) {
        this.mAnswers = mFeedItems;

        anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(0);
        anim.setDuration(300);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_ans_list_item, parent, false);

        baseline = ContextCompat.getDrawable(mContext, R.drawable.post_likeblue_icon);
        outline = ContextCompat.getDrawable(mContext, R.drawable.post_like_icn);
        bookMarkBaseline = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_bookmark_24px);
        bookMarkOutline = ContextCompat.getDrawable(mContext, R.drawable.ic_outline_bookmark_border_24px);

        return new AnswerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Answer answer = mAnswers.get(position);

        if (answer != null) {
            long comments=answer.getComments();
            AnswerHolder answerHolder = (AnswerHolder) holder;
            answerHolder.wireViews(comments);

            List<Comment> mComments=new ArrayList<>();
            CommentsAdapter commentsAdapter = setUpCommentsAdapter(answerHolder, mComments);

            User publisher = new User();
            final String answerPublisherId = answer.getUserId();
            final String itemContent = answer.getContent();
            final String imagePosted = answer.getImgUrl();
            final String imageThumb = answer.getImgThmb();
            final String timeAgo = convertDateToAgo(answer.getPublishTime());
            String answerId = answer.getFeedItemId();
            if(quesId==null)
                quesId = answer.getQuesId();
            long views=answer.getViews();
            long upVotes=answer.getUpVotes();

            publisher.setUserId(answerPublisherId);

            bindAnswer(answerHolder, itemContent, imagePosted, imageThumb, timeAgo);
            bindAnswererData(answerHolder, publisher);

            bindViewsUpvotes(answerHolder, views, upVotes, quesId, answerPublisherId, answerId, answer.isCrack());

            commentBtnListener(answerHolder,quesId,answerId, answerId);
            seeAllCommentsListener(answerHolder, quesId, answerId, mComments, commentsAdapter);

            overflowListener(answerHolder, publisher, answer);
            answererProfileListener(answerHolder, publisher, answerId);

            checkIfAlreadyBookMarked(answerHolder, answerId);
            //handleIfAlreadyUpVoted(answerHolder, answerId);

            bookMarkBtnListener(answerHolder, answerId, answerPublisherId);
            likeBtnListener(answerHolder,answerId,answerPublisherId,upVotes,answer.getType());

        }
    }



    public void insertDeleteBookMarks() {
            insertIncrementBookMarks();
            deleteDecrementBookMarks();
    }
    public void insertDeleteUpVotes() {
        insertIncrementUpVotes();
        deleteDecrementUpVotes();
    }

    private void insertIncrementBookMarks() {

        for(final Bookmark bookmark: addBookMarks){
            mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                    .collection("bookmarks").add(bookmark).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    mFirebaseUtil.mFirestore.collection("users").document(bookmark.getPublisherId())
                            .collection("answers").document(bookmark.getBookMarkItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                            noOfBookMarks = noOfBookMarks + 1;

                            Map<String, Object> map = new HashMap<>();
                            map.put("bookMarks", noOfBookMarks);

                            mFirebaseUtil.mFirestore.collection("users").document(bookmark.getPublisherId())
                                    .collection("answers").document(bookmark.getBookMarkItemId()).update(map);

                            mFirebaseUtil.mFirestore.collection("questions").document(quesId)
                                    .collection("answers").document(bookmark.getBookMarkItemId()).update(map);

                        }
                    });
                }
            });

        }


    }

    private void deleteDecrementBookMarks() {
        for(final Bookmark bookmark: removeBookMarks){
            CollectionReference collectionReference = mFirebaseUtil.mFirestore.collection("users")
                    .document(mUserId).collection("bookmarks");
            Query query = collectionReference.whereEqualTo("bookMarkItemId", bookmark.getBookMarkItemId());

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    DocumentSnapshot documentSnapshot;
                    String id = null;
                    if(documents.size()>=1) {
                        documentSnapshot = documents.get(0);
                        id = documentSnapshot.getId();
                    }
                    if(id!=null)
                    mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                            .collection("bookmarks").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFirebaseUtil.mFirestore.collection("users").document(bookmark.getPublisherId())
                                    .collection("answers").document(bookmark.getBookMarkItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    long noOfBookMarks = (long) documentSnapshot.get("bookMarks");

                                    noOfBookMarks = noOfBookMarks - 1;

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("bookMarks", noOfBookMarks);

                                    mFirebaseUtil.mFirestore.collection("users").document(bookmark.getPublisherId())
                                            .collection("answers").document(bookmark.getBookMarkItemId()).update(map);

                                    mFirebaseUtil.mFirestore.collection("questions").document(quesId)
                                            .collection("answers").document(bookmark.getBookMarkItemId()).update(map);

                                }
                            });

                        }
                    });

                }
            });
        }


    }

    private void insertIncrementUpVotes() {

        for(final UpVote upVote: addUpVotes){
            mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                    .collection("up_votes").document(upVote.getFeedItemId()).set(upVote).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mFirebaseUtil.mFirestore.collection("users").document(upVote.getPublisherId())
                            .collection("answers").document(upVote.getFeedItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            long noOfUpVotes = (long) documentSnapshot.get("upVotes");

                            Map<String, Object> map = new HashMap<>();
                            map.put("upVotes", noOfUpVotes+1);

                            mFirebaseUtil.mFirestore.collection("users").document(upVote.getPublisherId())
                                    .collection("answers").document(upVote.getFeedItemId()).update(map);

                            mFirebaseUtil.mFirestore.collection("questions").document(quesId)
                                    .collection("answers").document(upVote.getFeedItemId()).update(map);

                        }
                    });
                }
            });
        }


    }

    private void deleteDecrementUpVotes() {
        for(final UpVote upVote: removeUpVotes){

            mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                    .collection("up_votes").document(upVote.getFeedItemId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                                mFirebaseUtil.mFirestore.collection("users").document(upVote.getPublisherId()).collection("answers").
                                        document(upVote.getFeedItemId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        long noOfUpVotes = (long) documentSnapshot.get("upVotes");

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("upVotes", noOfUpVotes-1);

                                        mFirebaseUtil.mFirestore.collection("users").document(upVote.getPublisherId())
                                                .collection("answers").document(upVote.getFeedItemId()).update(map);

                                        mFirebaseUtil.mFirestore.collection("questions").document(quesId)
                                                .collection("answers").document(upVote.getFeedItemId()).update(map);

                                    }
                                });

                        }});

        }




    }

    private void checkIfAlreadyBookMarked(final AnswerHolder answerHolder, final String ansId) {
        mFirebaseUtil.mFirestore.collection("users").document(mUserId)
                .collection("bookmarks").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_bookmark_24px);
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot docuSnapshot : documents) {
                    String bookMarkItemId=null;

                    Bookmark bookmark = docuSnapshot.toObject(Bookmark.class);
                    if(bookmark!=null) {
                        bookMarkItemId = bookmark.getBookMarkItemId();
                    }
                    if(bookMarkItemId != null) {
                        if (bookMarkItemId.equals(ansId)) {
                            prevBkMarkedAnsIds.add(ansId);
                            answerHolder.bookmarkIview.setImageDrawable(drawable);
                            if(drawable != null)
                                drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));

                        }

                    }
                }
            }
        });
    }

    private void handleIfAlreadyUpVoted(final AnswerHolder answerHolder, final String ansId) {
        prevUpVotedAnsIds.add(ansId);
        answerHolder.likeBtn.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.post_likeblue_icon));

    }



    private void bookMarkBtnListener(final AnswerHolder answerHolder, final String answerId, final String answerPublisherId) {
        answerHolder.bookmarkIview.setOnClickListener(new View.OnClickListener() {

            Bookmark bookmark;

            @Override
            public void onClick(View view) {

                bookmark = new Bookmark(answerId, answerPublisherId, BookMarkType.ANSWER);
                if(!prevBkMarkedAnsIds.contains(answerId)) {
                    addBookMarks.add(bookmark);
                    prevBkMarkedAnsIds.add(answerId);
                    answerHolder.bookmarkIview.setImageDrawable(bookMarkBaseline);
                    if (bookMarkBaseline != null)
                        bookMarkBaseline.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                }else{
                    removeBookMarks.add(bookmark);
                    addBookMarks.remove(bookmark);
                    prevBkMarkedAnsIds.remove(answerId);
                    answerHolder.bookmarkIview.setImageDrawable(bookMarkOutline);
                    if (bookMarkOutline != null)
                        bookMarkOutline.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
                }

                answerHolder.bookmarkIview.startAnimation(anim);
            }

        });
    }

    private void likeBtnListener(final AnswerHolder answerHolder, final String answerId, final String answerPublisherId, final long upVotes, final int type) {
        answerHolder.likeBtn.setOnClickListener(new View.OnClickListener() {

            UpVote upVote;

            @Override
            public void onClick(View view) {
                if(type==FeedItem.CRACK)
                    upVote = new UpVote(answerId, answerPublisherId, BookMarkType.CRACK);
                else
                    upVote = new UpVote(answerId, answerPublisherId, BookMarkType.ANSWER);

                if(!prevUpVotedAnsIds.contains(answerId)) {
                    addUpVotes.add(upVote);
                    prevUpVotedAnsIds.add(answerId);
                    answerHolder.likeBtn.setImageDrawable(baseline);
                    answerHolder.likesCount.setText(upVotes+1+" Up Votes");

                }else{
                    removeUpVotes.add(upVote);
                    addUpVotes.remove(upVote);
                    prevUpVotedAnsIds.remove(answerId);
                    answerHolder.likeBtn.setImageDrawable(outline);
                    answerHolder.likesCount.setText(upVotes-1+" Up Votes");

                }

                answerHolder.bookmarkIview.startAnimation(anim);
            }

        });
    }

    @NonNull
    private CommentsAdapter setUpCommentsAdapter(AnswerHolder answerHolder, List<Comment> mComments) {
        CommentsAdapter commentsAdapter=new CommentsAdapter(mComments);
        answerHolder.commentsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        answerHolder.commentsRecycler.setLayoutManager(layoutManager);
        answerHolder.commentsRecycler.setAdapter(commentsAdapter);
        return commentsAdapter;
    }

    private void seeAllCommentsListener(final AnswerHolder answerHolder, final String quesId, final String answerId,
                                        final List<Comment> mComments, final CommentsAdapter commentsAdapter) {

        answerHolder.seeAllComments.setOnClickListener(new View.OnClickListener() {
            int flag=1;
            @Override
            public void onClick(View view) {
                if(flag==1)
                mFirebaseUtil.mFirestore.collection("questions").document(quesId).collection("answers").
                        document(answerId).collection("comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot documentSnapshot: documents) {
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            mComments.add(comment);
                        }
                        commentsAdapter.notifyDataSetChanged();
                    }
                });
                flag=0;
            }
        });
    }

    private void commentBtnListener(AnswerHolder answerHolder, final String quesId, final String answerId, final String answererId) {
        answerHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("EXTRA_QUESTION_ID", quesId);
                intent.putExtra("EXTRA_FEED_ITEM_ID", answerId);
                intent.putExtra("EXTRA_PUBLISHER_IDDD", answererId);
                intent.putExtra("IF_FROM_FULL_ANSWER_ACTIVITY", true);
                mContext.startActivity(intent);
            }
        });
        answerHolder.commentsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("EXTRA_QUESTION_ID", quesId);
                intent.putExtra("EXTRA_FEED_ITEM_ID", answerId);
                intent.putExtra("EXTRA_PUBLISHER_IDDD", answererId);
                intent.putExtra("IF_FROM_FULL_ANSWER_ACTIVITY", true);
                mContext.startActivity(intent);
            }
        });
    }

    public void bindViewsUpvotes(AnswerHolder answerHolder, long views, long upVotes, String questionId, String answerPublisherId, String answerId, boolean isCrack) {
        try {
            //store it in his activity log
            //generate edge rank
            //notify publisher
            //notify his followers

            //upVoteListner(answerHolder, upVotes, questionId, answerPublisherId, answerId, isCrack);

            //bindViews(answerHolder, views, upVotes);
            bindUpVotes(answerHolder, upVotes);

            new ViewChecker().execute(answerHolder,views,questionId,answerPublisherId,answerId,isCrack);
            new UpVoteChecker().execute(answerHolder,answerId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindUpVotes(AnswerHolder answerHolder, long upVotes) {
            answerHolder.likesCount.setText(upVotes+" Up votes");
    }

    private void bindViews(AnswerHolder answerHolder, long views, Boolean isViewed) {
        if(isViewed)
            answerHolder.viewsText.setText(views + " Views");
        else
            answerHolder.viewsText.setText(views+1+ " Views");
    }

//    private void upVoteListner(AnswerHolder answerHolder, long upVotes, String questionId, String answerPublisherId, String answerId, boolean isCrack, Boolean isUpVoted) {
//        answerHolder.likeBtn.setOnClickListener(
//                new UpVoteListener(answerHolder.likeBtn, answerHolder.likesCount, answerHolder.flag, answerPublisherId, answerId, upVotes, (Activity) mContext, true, questionId, isCrack, isUpVoted)
//        );
//    }

    private void incrementViews(long views, String questionId, String answerPublisherId, String answerId, boolean isCrack) {
        Map<String, Object> map= new HashMap<>();
        map.put("views",views+1);

        mFirebaseUtil.mFirestore.collection("users").document(answerPublisherId).collection("answers").document(answerId).update(map);
        mFirebaseUtil.mFirestore.collection("questions").document(questionId).collection("answers").document(answerId).update(map);

        Map<String, Object> map1= new HashMap<>();
        map1.put("publisherId",answerPublisherId);
        if(isCrack)
            map1.put("feedItemType", FeedItem.CRACK);
        else
            map1.put("feedItemType", FeedItem.ANSWER);

        mFirebaseUtil.mFirestore.collection("users").document(mUserId).collection("views").document(answerId).set(map1);
    }




    private void overflowListener(AnswerHolder answerHolder, User publisher, Answer answer) {
        answerHolder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, publisher, OverflowType.POST_ANS_CRACK, answer));
    }

    private void answererProfileListener(final AnswerHolder answerHolder, final User publisher, final String postId) {
        answerHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

        answerHolder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    private void bindAnswer(@NonNull AnswerHolder holder, final String answer, final String publishedImg, final String publishedThumb, String timeAgo) {
        holder.setContent(answer);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
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

    public class AnswerHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView answerContent;
        private TextView userName;
        private CircleImageView userImage;
        private TextView timeAgo;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private TextView viewsText;
        private TextView likesCount;
        private ImageView likeBtn;
        private CircleImageView commentsImage;
        private TextView commentBtn;
        private TextView seeAllComments;
        private ImageView bookmarkIview;
        private RecyclerView commentsRecycler;

        private boolean flag = true;

        public AnswerHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(long comments){
            userImage = mView.findViewById(R.id.profile_image);
            userName = mView.findViewById(R.id.userNameTView);
            postedImgView = mView.findViewById(R.id.postedImage);
            timeAgo = mView.findViewById(R.id.feed_time);
            overflowImgView = mView.findViewById(R.id.overflow);
            answerContent = mView.findViewById(R.id.full_text);
            viewsText = mView.findViewById(R.id.viewsText);
            likesCount = mView.findViewById(R.id.likesCount);
            likeBtn = mView.findViewById(R.id.full_post_like);
            bookmarkIview = mView.findViewById(R.id.full_post_bookmark);
            commentsImage = mView.findViewById(R.id.commentsImage);
            commentBtn = mView.findViewById(R.id.commentBtn);
            seeAllComments = mView.findViewById(R.id.seeAllComments);
            commentsRecycler = mView.findViewById(R.id.commentsRecycler);
            if(comments==0)
                seeAllComments.setVisibility(View.INVISIBLE);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            Glide.with(mContext.getApplicationContext()).load(FeedFragment.sDpUrl).apply(placeholderOption).into(commentsImage);
        }


        public void setContent(String answer) {
            answerContent.setText(answer);

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
            this.timeAgo.setText(timeAgo);
        }

        public void setUserData(String name, String image) {
            userName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (image!=null&&null!=mContext)
                Glide.with(mContext.getApplicationContext()).load(image).apply(placeholderOption).into(userImage);
        }

    }

    public class ViewChecker extends AsyncTask<Object,Void,Boolean> {
        AnswerHolder answerHolder;
        long views;
        String questionId;
        String answerPublisherId;
        String answerId;
        boolean isCrack;

        @Override
        protected Boolean doInBackground(Object... objects) {
            answerHolder=(AnswerHolder) objects[0];
            views=(long) objects[1];
            questionId=(String)objects[2];
            answerPublisherId=(String)objects[3];
            answerId=(String)objects[4];
            isCrack=(boolean)objects[5];

            Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.
                    collection("users").document(mUserId).collection("views").document(answerId).get();
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
            bindViews(answerHolder,views,isViewed);
            if(!isViewed)
                incrementViews(views, questionId, answerPublisherId, answerId, isCrack);
        }
    }

    public class UpVoteChecker extends AsyncTask<Object,Void,Boolean> {
        AnswerHolder answerHolder;
        String answerId;

        @Override
        protected Boolean doInBackground(Object... objects) {
            answerHolder=(AnswerHolder) objects[0];
            answerId=(String)objects[1];

            Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.
                            collection("users").document(mUserId).collection("up_votes").document(answerId).get();
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
            if(isUpVoted)
                handleIfAlreadyUpVoted(answerHolder,answerId);

        }
    }


}
