package com.learnexo.main;

import android.content.Context;
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
import com.learnexo.model.feed.likediv.Comment;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Comment> mComments;
    private Context mContext;

    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public UserCommentsRecyclerAdapter(List<Comment> comments) {
        this.mComments = comments;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_list_item, parent, false);
        return new AllCommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AllCommentsHolder allCommentsHolder = (AllCommentsHolder) holder;
        allCommentsHolder.wireViews();
     //   bindPost(allCommentsHolder, );

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    private void bindPost(@NonNull AllCommentsHolder holder, final String answer, String timeAgo) {
        holder.setContent(answer);
        holder.setTime(timeAgo);
        holder.setUserData(FeedFragment.sName, FeedFragment.sDpUrl);
    }

    public class AllCommentsHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView circleImageView;
        private TextView userName;
        private TextView answeredTime;
        private ImageView overflowImgView;
        private TextView postContent;
        private TextView replyTView;
        private TextView upvoteTView;
        private TextView downvoteTView;

        public AllCommentsHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            circleImageView = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            answeredTime = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);
            postContent = mView.findViewById(R.id.answerContent);
            replyTView = mView.findViewById(R.id.replyTView);
            upvoteTView = mView.findViewById(R.id.upvoteTView);
            downvoteTView = mView.findViewById(R.id.downvoteTView);
        }

        public void setContent(String answer) {
            postContent.setText(answer);
            postContent.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = postContent.getLineCount();
                    if(lineCount>=3) {}
                       // seeMore.setVisibility(View.VISIBLE);
                }
            });

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
    }
}
