package com.learnexo.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learnexo.model.user.Follow;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Follow> mFollowList;
    private Context mContext;
    private FirebaseUtil mFireBaseUtil = new FirebaseUtil();

    public FollowListAdapter(List<Follow> mFollowList) {
        this.mFollowList = mFollowList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.follow_list_item, parent, false);
        return new AllFollowListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Follow follow = mFollowList.get(position);

        String userImage = follow.getDpUrl();
        String userName = follow.getFirstName();
        String description = follow.getDescription();

        AllFollowListHolder allFollowListHolder = (AllFollowListHolder) holder;
        allFollowListHolder.wireViews();
        bindPost(allFollowListHolder, userImage, userName, description);

    }

    @Override
    public int getItemCount() {
        return mFollowList.size();
    }

    private void bindPost(@NonNull AllFollowListHolder holder, final String userImage, final String userName,
                          final String description) {
        holder.setDetails(userImage, userName, description);
    }

    public class AllFollowListHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView userImage;
        private TextView userName;
        private TextView description;

        public AllFollowListHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void wireViews() {

            userImage = mView.findViewById(R.id.userImage);
            userName = mView.findViewById(R.id.userName);
            description = mView.findViewById(R.id.description);

        }

        public void setDetails(String userImagee, String userNamee, String userDescription) {

            userName.setText(userNamee);
            description.setText(userDescription);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.empty_profilee);
            if (userImagee!=null&&null!=mContext)
                Glide.with(mContext.getApplicationContext()).load(userImagee).apply(placeholderOption).into(userImage);

        }

    }
}
