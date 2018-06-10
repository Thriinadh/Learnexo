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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private String mFeedItemId;
    private static final String TAG=OverflowMenuListener.class.getSimpleName();

    List<String> menuItems =new ArrayList<>();
    List<Drawable> iconList =new ArrayList<>();

    public OverflowMenuListener(Context context, User publisher, OverflowType overflowType, FeedItem feedItem) {
        bindData(context, publisher, feedItem);

        if(mFeedItemId!=null)
            checkIfAlreadyFollowingFeedItem(mFeedItemId);

        if(overflowType==OverflowType.POST_ANS_CRACK) {
            buildPostAnsCrackItems();
            checkIfAlreadyFollowingPublisher(mPublisherId, mCurrentUserId);
        }
        else if(overflowType==OverflowType.QUES_CHALLENGE) {
            buildQuesChallengeItems();


        }
    }


    @Override
    public void onClick(View view) {

        BottomSheet.Builder builder = getBuilder();

        itemClickListener(builder);


    }


    private void bindData(Context context, User publisher, FeedItem feedItem) {
        mContext = context;
        this.publisher = publisher;
        mPublisherId=publisher.getUserId();
        publisherName=feedItem.getUserName();
        mFeedItemId=feedItem.getFeedItemId();
    }

    private void buildQuesChallengeItems() {
        menuItems.add("Up Vote Question");
        menuItems.add("Turn ON Notifications");
        menuItems.add("Add a Comment on Question");// view 1 comment
        menuItems.add("Down Vote Question");
        menuItems.add("Share");
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

    private void buildPostAnsCrackItems() {
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


    private void itemClickListener(BottomSheet.Builder builder) {
        builder.listener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence title = item.getTitle();
                String strTitle=(String)title;
                if(strTitle.startsWith("Follow")&&!mPublisherId.equals(mCurrentUserId)){
                    followListener();
                }else if (strTitle.startsWith("Un Follow")&&!mPublisherId.equals(mCurrentUserId)){
                    unFollowListener();
                }else{
                    switch (strTitle){
                        case "Turn ON Notifications":
                            turnOnNotifications(mCurrentUserId,mPublisherId,mFeedItemId);
                            break;
                        case "Turn OFF Notifications":
                            if(mFeedItemId!=null)
                            turnOFFNotifications(mCurrentUserId,mFeedItemId);
                            break;


                    }


                }




                return true;
            }
        });
    }

    @NonNull
    private BottomSheet.Builder getBuilder() {
        final BottomSheet.Builder builder = new BottomSheet.Builder((Activity) mContext);
        for (int i = 0; i<menuItems.size(); i++) {
            String s = menuItems.get(i);
            builder.sheet(i,iconList.get(i), s);
        }
        builder.build();
        builder.show();
        return builder;
    }


    private void checkIfAlreadyFollowingFeedItem(String feedItemId) {
        mFirebaseUtil.mFirestore.collection("users").document(mCurrentUserId).collection("following_feed_items").document(feedItemId).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        menuItems.set(1,"Turn OFF Notifications");
                        iconList.set(1,ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }


            }
        });

    }

    private void checkIfAlreadyFollowingPublisher(String publisherId, String currentUserId) {
        DocumentReference docRef = mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("following").document(publisherId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        menuItems.set(0,"Un Follow "+publisherName);
                        iconList.set(0,ContextCompat.getDrawable(mContext, R.drawable.ic_outline_person_add_24px));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }








    private void turnOFFNotifications(String currentUserId, String feedItemId) {
        mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("following_feed_items").
                document(feedItemId).delete();
        Toast.makeText(mContext, "You un followed this item", Toast.LENGTH_SHORT).show();
    }

    private void turnOnNotifications(String currentUserId, String publisherId, String feedItemId) {
        Map<String,Object> map=new HashMap<>();
        map.put("publisherId",publisherId);
        map.put("feedItemId",feedItemId);

        mFirebaseUtil.mFirestore.collection("users").document(currentUserId).collection("following_feed_items").
                document(feedItemId).set(map);
        Toast.makeText(mContext, "Now You are following this item", Toast.LENGTH_SHORT).show();


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
    private void unFollowListener() {

        mFirebaseUtil.mFirestore.collection("users").document(mCurrentUserId)
                .collection("following").document(mPublisherId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFirebaseUtil.mFirestore.collection("users").document(mPublisherId)
                        .collection("followers").document(mCurrentUserId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "You Un followed "+publisher.getFirstName(), Toast.LENGTH_SHORT).show();
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
