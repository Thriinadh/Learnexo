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
import com.learnexo.util.FirebaseUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedItemHolder> {

    private List<FeedItem> mFeedItems;
    private Context context;
    private FirebaseUtil mFirebaseUtil = new FirebaseUtil();


    public FeedRecyclerAdapter(List<FeedItem> mFeedItems) {
        this.mFeedItems = mFeedItems;
    }

    @NonNull
    @Override
    public FeedItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
        return new FeedItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedItemHolder holder, int position) {
        FeedItem feedItem=mFeedItems.get(position);

        final String user_id = feedItem.getUserId();
        final String itemContent = feedItem.getContent();
        final String imagePosted = feedItem.getImgUrl();
        final String timeAgo = convertDateToAgo(feedItem.getPublishTime());

        bind(holder, itemContent, imagePosted, timeAgo);
        bindUserData(holder, user_id);

        contentListener(holder, itemContent, imagePosted, timeAgo);
        overflowImgViewListener(holder, user_id);


    }

    private void contentListener(@NonNull FeedItemHolder holder, final String itemContent, final String imagePosted, final String timeAgo) {
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FullPostActivity.newIntent(context, itemContent, imagePosted, timeAgo);
                context.startActivity(intent);
            }
        });
    }

    private void bindUserData(@NonNull final FeedItemHolder holder, String user_id) {
        mFirebaseUtil.mFirestore.collection("users").document(user_id).
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

                } else {
                    // Error handling here
                }

            }
        });
    }

    private void overflowImgViewListener(@NonNull FeedItemHolder holder, final String user_id) {
        holder.setOverflowImgView();
        holder.overflowImgView.setOnClickListener(new View.OnClickListener() {

            private LinearLayout deleteBtnLayout;
            private LinearLayout editBtnLayout;
            private LinearLayout copyBtnLayout;
            private LinearLayout notifBtnLayout;
            private LinearLayout connectBtnLayout;

            @Override
            public void onClick(View view) {

                View bottomSheetView = View.inflate(context, R.layout.bottom_sheet_dialog_for_sharedposts, null);

                BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(bottomSheetView);
                dialog.show();

                inflateBottomSheetButtons(bottomSheetView);
            }

            private void inflateBottomSheetButtons(View bottomSheetView) {

                deleteBtnLayout = bottomSheetView.findViewById(R.id.deleteBtn);
                editBtnLayout = bottomSheetView.findViewById(R.id.editNameBtn);
                copyBtnLayout = bottomSheetView.findViewById(R.id.copyBtn);
                notifBtnLayout =  bottomSheetView.findViewById(R.id.notifBtn);
                connectBtnLayout =  bottomSheetView.findViewById(R.id.connectBtn);

                deleteBtnLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                       mFirebaseUtil.mFirestore.collection("users").document(user_id).collection("posts").document().delete()
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {

                               Toast.makeText(context, "deleted", Toast.LENGTH_LONG).show();

                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                               Log.d("FeedAdapter", "SomethingWentWrong "+e);

                           }
                       });

                    }
                });

            }
        });
    }

    private void bind(@NonNull FeedItemHolder holder, final String content, final String publishedImg, String timeAgo) {
        holder.setContent(content);
        holder.setPostedImgView(publishedImg);
        holder.setTime(timeAgo);
    }

    @Override
    public int getItemCount() {
        return mFeedItems.size();
    }



    // Inner class
    public class FeedItemHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView content;
        private TextView timeAgo;
        private TextView userName;
        private CircleImageView userImage;
        private ImageView overflowImgView;
        private ImageView postedImgView;

        // FeedItemHolder constructor
        public FeedItemHolder(View itemView) {
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
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_photo);
            Glide.with(context).load(image).apply(placeholderOption).into(userImage);

        }

        public void setOverflowImgView() {
            overflowImgView = mView.findViewById(R.id.overflow);
        }

    }

}
