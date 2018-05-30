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
import com.learnexo.model.feed.likediv.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Comment> mComments;
    private Context mContext;

    public CommentsAdapter(List<Comment> comments) {
        this.mComments = comments;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_list_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Comment comment = mComments.get(position);
        if(comment != null) {
            String content = comment.getComment();
            String timeAgo = convertDateToAgo(comment.getCommentTime());
            String publisherName = comment.getCommenterName();
            String publisherDp = comment.getCommenterDp();

            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.wireViews();
            bindPost(commentHolder, content, timeAgo, publisherDp, publisherName);
        }

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    private void bindPost(@NonNull CommentHolder holder, String answer, String timeAgo, String publisherDp, String publisherName) {
        holder.setContent(answer);
        holder.setTime(timeAgo);
        holder.setUserData(publisherName, publisherDp);
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView circleImageView;
        private TextView userName;
        private TextView answeredTime;
        private ImageView overflowImgView;
        private TextView postContent;
        private TextView replyTView;
        private TextView upvoteTView;
        private TextView downvoteTView;

        public CommentHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            circleImageView = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            answeredTime = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);
            postContent = mView.findViewById(R.id.commentData);
            replyTView = mView.findViewById(R.id.replyTView);
            upvoteTView = mView.findViewById(R.id.upvoteTView);
            downvoteTView = mView.findViewById(R.id.downvoteTView);
        }

        public void setContent(String answer) {
            postContent.setText(answer);

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
