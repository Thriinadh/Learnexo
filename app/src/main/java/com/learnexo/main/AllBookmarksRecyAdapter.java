package com.learnexo.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.learnexo.model.feed.FeedItem;
import com.learnexo.util.FirebaseUtil;

import java.util.List;

public class AllBookmarksRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FeedItem> mFeedItems;
    private Context mContext;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    public AllBookmarksRecyAdapter(List<FeedItem> mFeedItems) {
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
                return FeedItem.QUESTION;
            case 4:
                return FeedItem.CHALLENGE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
