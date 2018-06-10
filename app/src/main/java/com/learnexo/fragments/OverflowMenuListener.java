package com.learnexo.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.learnexo.main.R;
import com.learnexo.model.core.OverflowType;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverflowMenuListener implements View.OnClickListener {

    private String publisherName;
    private Context mContext;
    private User publisher;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private String mCurrentUserId=FirebaseUtil.getCurrentUserId();
    private String mPublisherId;

    List<String> menuItems =new ArrayList<>();
    List<Drawable> iconList =new ArrayList<>();

    public OverflowMenuListener(Context context, User publisher, OverflowType overflowType, FeedItem feedItem) {
        mContext = context;
        this.publisher = publisher;
        mPublisherId=publisher.getUserId();
        publisherName=feedItem.getUserName();

        checkIfFollowing(mPublisherId, mCurrentUserId);

        if(overflowType==OverflowType.POST_ANS_CRACK) {
            menuItems.add("Follow "+publisherName);//toggle
            menuItems.add("Turn ON Notifications");//toggle
            menuItems.add("Down Vote");//toggle - remove down vote
            menuItems.add("Report");
            //menuItems.add("Copy Link");//web
            //menuItems.add("Thank");
            //menuItems.add("Suggest Edits");
            //menuItems.add("Log");

            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px);
            iconList.add(drawable);
            drawable.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#1da1f2"), PorterDuff.Mode.SRC_IN));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_edit_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_delete_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_notifications_off_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_link_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_notifications_off_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_assignment_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_assignment_24px));
        }
        else if(overflowType==OverflowType.QUES_CHALLENGE) {
            menuItems.add("Up Vote Question");
            menuItems.add("Add a Comment on Question");// view 1 comment
            menuItems.add("Down Vote Question");
            menuItems.add("Share");
            menuItems.add("Turn On Notifications");
            menuItems.add("Report");
            //menuItems.add("Answer Later");
            //menuItems.add("Answer Anonymously");
            //menuItems.add("Follow Privately");
            //menuItems.add("View Status Log");
            //menuItems.add("Merge Question");
            //menuItems.add("View Status and Log");

            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_edit_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_delete_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_link_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px));
            iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_edit_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_delete_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_link_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_edit_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_delete_24px));
            //iconList.add(ContextCompat.getDrawable(mContext, R.drawable.ic_outline_delete_24px));


        }
    }

    private void checkIfFollowing(String publisherId, String currentUserId) {
        DocumentReference docRef = mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("following").document(publisherId);
        //ApiFuture<DocumentSnapshot> future = docRef.get();//

    }

    @Override
    public void onClick(View view) {

        BottomSheet.Builder builder = new BottomSheet.Builder((Activity) mContext);
        for (int i = 0; i<menuItems.size(); i++) {
             String s = menuItems.get(i);
             builder.sheet(i,iconList.get(i), s);
        }
        builder.build();
        builder.listener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence title = item.getTitle();
                String strTitle=(String)title;
                if(strTitle.contains("Follow")&&mPublisherId!=mCurrentUserId){
                    followListener();
                }


                return true;
            }
        });


        builder.show();


    }

    private void followListener() {
        final Map<String, Object> user = new HashMap<>();
        user.put("firstName", publisher.getFirstName());
        user.put("dpUrl", publisher.getDpUrl());
        user.put("userId", mPublisherId);

        mFirebaseUtil.mFirestore.collection("users").document(mCurrentUserId)
                .collection("following").document(mPublisherId).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        user.put("firstName", FeedFragment.sName);
                        user.put("dpUrl", FeedFragment.sDpUrl);
                        user.put("userId", mCurrentUserId);

                        mFirebaseUtil.mFirestore.collection("users").document(mPublisherId)
                                .collection("followers").document(mCurrentUserId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //  followTView.setText("Unfollow");
                                Toast.makeText(mContext, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(mContext, "SomethingWentWrong", Toast.LENGTH_LONG).show();

                Log.d("FeedAdapter", "SomethingWentWrong "+e);

            }
        });
    }

}
