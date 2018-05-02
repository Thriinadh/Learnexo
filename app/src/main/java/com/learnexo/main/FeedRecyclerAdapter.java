package com.learnexo.main;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.learnexo.model.feed.FeedItem;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.learnexo.util.DateUtil.convertDateToAgo;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    public  List<FeedItem> mFeedItems;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public FeedRecyclerAdapter(List<FeedItem> mFeedItems) {
        this.mFeedItems = mFeedItems;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);

        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, int position) {

        final String user_id = mFeedItems.get(position).getUserId();
        final String itemContent = mFeedItems.get(position).getContent();
        final String imagePosted = mFeedItems.get(position).getImgUrl();

        String timeAgo=convertDateToAgo(mFeedItems.get(position).getPublishTime());

        handleItemContent(holder, itemContent, imagePosted, timeAgo);

        handleOverflowIView(holder, user_id);

        getDPandName(holder, user_id);

        setPostedTime(holder, position);

    }

    private void setPostedTime(@NonNull PostHolder holder, int position) {
        if(mFeedItems.get(position).getPublishTime() != null) {

                Date date = mFeedItems.get(position).getPublishTime();

                String timeAgo = convertDateToAgo(date);
                holder.setTime(timeAgo);

        }
    }

    private void getDPandName(@NonNull final PostHolder holder, String user_id) {
        firebaseFirestore.collection("Users").document(user_id).
                collection("Setup Details").document("Setup Fields").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                        String name = task.getResult().getString("Nick name");
                        String image = task.getResult().getString("Image");

                        holder.setUserData(name, image);

                } else {

                    // Error handling here
                }

            }
        });
    }

    private void handleOverflowIView(@NonNull PostHolder holder, final String user_id) {
        holder.setOverflowIView();
        holder.overflowIView.setOnClickListener(new View.OnClickListener() {

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

                       firebaseFirestore.collection("Posts").document(user_id).delete()
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

    private void handleItemContent(@NonNull PostHolder holder, final String itemContent,
                                   final String imagePosted, final String posTime) {
        holder.setViewItemContent(itemContent);
        holder.setPostedIView(imagePosted);
        holder.contentTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = FullPostActivity.newIntent(context, itemContent, imagePosted, posTime);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeedItems.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView contentTView;
        private TextView dateTView;
        private TextView userNameTView;
        private CircleImageView userCircleIView;
        private ImageView overflowIView;
        private ImageView postedIView;

        // PostHolder constructor
        public PostHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setOverflowIView() {
            overflowIView = mView.findViewById(R.id.overflow);
        }

        public void setViewItemContent(String postedText) {
            contentTView = mView.findViewById(R.id.feed_content);
            contentTView.setText(postedText);
        }

        public void setPostedIView(String imageUrl) {
            postedIView = mView.findViewById(R.id.postedImagee);
//            if(imageUrl != null)
//            postedIView.setImageURI(Uri.parse(imageUrl));
            if(imageUrl != null)
            Glide.with(context).load(imageUrl).into(postedIView);
        }

        public void setTime(String date) {
            dateTView = mView.findViewById(R.id.feed_date);
            dateTView.setText(date);
        }

        public void setUserData(String name, String image) {
            userCircleIView = mView.findViewById(R.id.feed_user_image);
            userNameTView = mView.findViewById(R.id.userNameTView);
            userNameTView.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_photo);
            Glide.with(context).load(image).apply(placeholderOption).into(userCircleIView);

        }

    }

}
