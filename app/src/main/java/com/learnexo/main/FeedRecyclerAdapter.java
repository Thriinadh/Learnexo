package com.learnexo.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    public  List<FeedSharePostModel> feed_items_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    public FeedRecyclerAdapter(List<FeedSharePostModel> feed_items_list) {
        this.feed_items_list = feed_items_list;
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

        final String user_id = feed_items_list.get(position).getUser_id();
        final String itemContent = feed_items_list.get(position).getPostedContent();
        final String imagePosted = feed_items_list.get(position).getImage_url();

        handleItemContent(holder, itemContent, imagePosted);

        handleOverflowIView(holder, user_id);

        getDPandName(holder, user_id);

        setPostedTime(holder, position);

    }

    private void setPostedTime(@NonNull PostHolder holder, int position) {
        if(feed_items_list.get(position).getTimestamp() != null) {
            long millisecond = feed_items_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("yyyy.MM.dd 'AD at' HH:mm:ss", new Date(millisecond)).toString();

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
                Date past = format.parse(dateString);

                Date now = new Date();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

                if (seconds < 60) {
                    holder.setTime(seconds + " seconds ago");
                } else if (minutes < 60) {
                    holder.setTime(minutes + " minutes ago");
                } else if (hours < 24) {
                    holder.setTime(hours + " hours ago");
                } else {
                    if (days > 365) {
                        long years = days / 365;
                        if (years > 1)
                            holder.setTime(years + " years ago");
                        else
                            holder.setTime(years + " year ago");
                    }
                    holder.setTime(days + " days ago");
                }
            } catch (Exception j) {
                j.printStackTrace();
            }
        }
    }

    private void getDPandName(@NonNull final PostHolder holder, String user_id) {
        firebaseFirestore.collection("Users").document(user_id).
                collection("Setup Details").document("Setup Fields").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                           }
                       });

                    }
                });

            }
        });
    }

    private void handleItemContent(@NonNull PostHolder holder, final String itemContent,
                                   final String imagePosted) {
        holder.setViewItemContent(itemContent);
        holder.setPostedIView(imagePosted);
        holder.contentTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = FullPostActivity.newIntent(context, itemContent, imagePosted);
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feed_items_list.size();
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
