package com.learnexo.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.answer.Answer;
import com.learnexo.model.feed.post.PostDetails;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class AllAnsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Answer> mAnswers;
    private Context mContext;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();
    String path="answers";

    public AllAnsRecyclerAdapter(List<Answer> mFeedItems) {
        this.mAnswers = mFeedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_ans_list_item, parent, false);
        return new AllAnsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Answer answer = mAnswers.get(position);

        if (answer != null) {
            User publisher = new User();
            final String answererId = answer.getUserId();
            final String itemContent = answer.getContent();
            final String imagePosted = answer.getImgUrl();
            final String imageThumb = answer.getImgThmb();
            final String timeAgo = convertDateToAgo(answer.getPublishTime());
            String answerId = answer.getFeedItemId();
            String quesId = answer.getQuesId();

            publisher.setUserId(answererId);

            AllAnsHolder allAnsHolder = (AllAnsHolder) holder;
            allAnsHolder.wireViews();
            bindAnswer(allAnsHolder, itemContent, imagePosted, imageThumb, timeAgo);
            bindAnswererData(allAnsHolder, publisher);

            new GetViewsAndUpVotes().execute(answererId, answerId, path, allAnsHolder, quesId);

            allAnsOverflowListener(allAnsHolder, publisher, answer);
            answersProfileListener(allAnsHolder, publisher, answerId);

        }
    }


    private void allAnsOverflowListener(AllAnsHolder allAnsHolder, User publisher, Answer answer) {
        allAnsHolder.overflowImgView.setOnClickListener(new PostAnsCrackItemOverflowListener(mContext, publisher));
    }

    private void answersProfileListener(final AllAnsHolder allAnsHolder, final User publisher, final String postId) {
        allAnsHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = OthersProfileActivity.newIntent(mContext, publisher);
                mContext.startActivity(intent);
            }
        });

        allAnsHolder.userName.setOnClickListener(new View.OnClickListener() {
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

    private void bindAnswer(@NonNull AllAnsHolder holder, final String answer, final String publishedImg, final String publishedThumb, String timeAgo) {
        holder.setContent(answer);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
    }

    private void bindAnswererData(@NonNull final AllAnsHolder holder, final User user) {
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

    public class AllAnsHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView answerContent;
        private TextView userName;
        private CircleImageView userImage;
        private TextView timeAgo;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private TextView viewsText;
        private TextView likesCount;
        private ImageView LikeBtn;

        private long upVotes;
        private long views;
        private boolean flag = true;

        public AllAnsHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            userImage = mView.findViewById(R.id.profile_image);
            userName = mView.findViewById(R.id.userNameTView);
            postedImgView = mView.findViewById(R.id.postedImage);
            timeAgo = mView.findViewById(R.id.feed_time);
            overflowImgView = mView.findViewById(R.id.overflow);
            answerContent = mView.findViewById(R.id.full_text);
            viewsText = mView.findViewById(R.id.viewsText);
            likesCount = mView.findViewById(R.id.likesCount);
            LikeBtn = mView.findViewById(R.id.full_post_like);
        }

        public void bindViewsUpvotes(PostDetails postDetails, String questionId, String answererId, String answerId) {
            try {
                upVotes=postDetails.getNoOfLikes();
                views=postDetails.getNoOfViews();

                //color the like button and increase the likes in the UI
                //save new no of likes
                //store it in his activity log
                //generate edge rank
                //notify publisher
                //notify his followers
                LikeBtn.setOnClickListener(
                        new LikeBtnListener(LikeBtn,likesCount,flag, answererId, answerId, upVotes, (Activity) mContext, true, questionId)
                );

                likesCount.setText(upVotes+" Up votes");
                if(views==0){
                    views=1;
                    viewsText.setText("1 View");
                }else{
                    viewsText.setText(views+ " Views");
                }


                long viewss = views+1;
                Map<String, Object> map= new HashMap();
                map.put("views",viewss);

                mFirebaseUtil.mFirestore.collection("users").
                        document(answererId).
                        collection("answers").
                        document(answerId).update(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public class GetViewsAndUpVotes extends AsyncTask<Object, Void,PostDetails> {

        private AllAnsHolder mAllAnsHolder;
        private String answererId;
        private String answerId;
        private String questionId;
        @Override
        protected PostDetails doInBackground(Object[] objects) {
            mAllAnsHolder= (AllAnsHolder) objects[3];
            questionId= (String) objects[4];
            answererId= (String) objects[0];
            answerId= (String) objects[1];

            Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("users").
                    document(answererId).collection((String) objects[2]).document(answerId).get();
            PostDetails postDetails=null;


            try {
                DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);

                postDetails = new PostDetails();
                postDetails.setNoOfLikes((Long) documentSnapshot.get("upVotes"));
                postDetails.setNoOfViews((Long) documentSnapshot.get("views"));

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return postDetails;
        }

        @Override
        protected void onPostExecute(PostDetails result) {
            mAllAnsHolder.bindViewsUpvotes(result, questionId, answererId, answerId);
        }


    }

}
