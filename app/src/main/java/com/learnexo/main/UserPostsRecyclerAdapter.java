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
import com.learnexo.fragments.PostAnsCrackItemOverflowListener;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class UserPostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> mPosts;
    private Context mContext;

    private final String mCurrentUserId = FirebaseUtil.getCurrentUserId();
    User mUser = new User(mCurrentUserId,FeedFragment.sName,FeedFragment.sDpUrl);

    public UserPostsRecyclerAdapter(List<Post> mFeedItems) {
        this.mPosts = mFeedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_list_item, parent, false);
        return new AllPostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = mPosts.get(position);

        if (post != null) {

            final String itemContent = post.getContent();
            final String imagePosted = post.getImgUrl();
            final String imageThumb = post.getImgThmb();
            final String timeAgo = convertDateToAgo(post.getPublishTime());

            AllPostsHolder allPostsHolder = (AllPostsHolder) holder;
            allPostsHolder.wireViews();
            bindPost(allPostsHolder, itemContent, imagePosted, imageThumb, timeAgo);
            allPostsOverflowListener(allPostsHolder, post);

        }
    }

    private void allPostsOverflowListener(AllPostsHolder allPostsHolder, Post post) {
        allPostsHolder.overflowImgView.setOnClickListener(new PostAnsCrackItemOverflowListener(mContext, mUser));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private void bindPost(@NonNull AllPostsHolder holder, final String answer, final String publishedImg, final String publishedThumb, String timeAgo) {
        holder.setContent(answer);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
        holder.setUserData(FeedFragment.sName, FeedFragment.sDpUrl);
    }


    public class AllPostsHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView postContent;
        private TextView userName;
        private CircleImageView circleImageView;
        private TextView answeredTime;
        private ImageView overflowImgView;
        private ImageView postedImgView;
        private TextView seeMore;

        public AllPostsHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews(){
            circleImageView = mView.findViewById(R.id.circleImageView);
            userName = mView.findViewById(R.id.userName);
            postedImgView = mView.findViewById(R.id.postedImagee);
            answeredTime = mView.findViewById(R.id.answeredTime);
            overflowImgView = mView.findViewById(R.id.overflow);
            postContent = mView.findViewById(R.id.answerContent);
            seeMore = mView.findViewById(R.id.seeMore);
        }

        public void setContent(String answer) {
            postContent.setText(answer);
            postContent.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = postContent.getLineCount();
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

    }

}
