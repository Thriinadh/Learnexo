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
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class UserPostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> mPosts;
    private Context mContext;

    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();
    private final String mCurrentUserId = FirebaseUtil.getCurrentUserId();
    private boolean isOtherProfile;
    private String otherProfileId;
    private String otherProfileName;
    private String otherProfileDP;

    User mUser;

    public UserPostsRecyclerAdapter(List<Post> mFeedItems, boolean isOtherProfile, String otherProfileId, String otherProfileName, String  otherProfileDP) {
        this.mPosts = mFeedItems;
        this.isOtherProfile=isOtherProfile;
        this.otherProfileId=otherProfileId;
        this.otherProfileName=otherProfileName;
        this.otherProfileDP=otherProfileDP;
        if(isOtherProfile)
            mUser = new User(otherProfileId,otherProfileName,otherProfileDP);
        else
            mUser = new User(mCurrentUserId,FeedFragment.sName,FeedFragment.sDpUrl);
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
            User publisher =new User();
            publisher.setUserId(post.getUserId());
            publisher.setFirstName(FeedFragment.sName);
            publisher.setDpUrl(FeedFragment.sDpUrl);

            String itemContent = post.getContent();
            String imagePosted = post.getImgUrl();
            String imageThumb = post.getImgThmb();
            String feedItemId = post.getFeedItemId();
            String timeAgo = convertDateToAgo(post.getPublishTime());
            long views = post.getViews();
            long upVotes = post.getUpVotes();
            long comments = post.getComments();

            AllPostsHolder allPostsHolder = (AllPostsHolder) holder;
            allPostsHolder.wireViews();
            bindPost(allPostsHolder, itemContent, imagePosted, imageThumb, timeAgo);
            postContentListener(allPostsHolder,itemContent, imagePosted, imageThumb, timeAgo, publisher, feedItemId, comments, views, upVotes);
            allPostsOverflowListener(allPostsHolder, post);

        }
    }

    private void allPostsOverflowListener(AllPostsHolder allPostsHolder, Post post) {
        allPostsHolder.overflowImgView.setOnClickListener(new OverflowMenuListener(mContext, mUser, OverflowType.POST_ANS_CRACK,post));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    private void postContentListener(AllPostsHolder allPostsHolder, final String itemContent, final String imagePosted, final String imageThumb,
                                     final String timeAgo, final User publisher, final String postId,
                                     final long comments, final long views, final long upVotes) {
        allPostsHolder.postContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        Intent intent = FullPostActivity.newIntent(mContext, itemContent, imagePosted, imageThumb, timeAgo, publisher, postId, comments, views, upVotes);
        mContext.startActivity(intent);
            }
        });


    }

    private void bindPost(@NonNull AllPostsHolder holder, final String answer, final String publishedImg,
                          final String publishedThumb, String timeAgo) {
        holder.setContent(answer);
        holder.setAnsImgView(publishedImg, publishedThumb);
        holder.setTime(timeAgo);
        holder.setUserData(mUser.getFirstName(), mUser.getDpUrl());
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
