package com.learnexo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.learnexo.main.R;
import com.learnexo.model.user.User;
import com.learnexo.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class PostAnsCrackItemOverflowListener implements View.OnClickListener {
    private TextView followTView;
    private LinearLayout followBtnLayout;
    private LinearLayout deleteBtnLayout;
    private LinearLayout editBtnLayout;
    private LinearLayout copyBtnLayout;
    private LinearLayout notifBtnLayout;
    private LinearLayout connectBtnLayout;

    private Context mContext;
    private Dialog mDialog;
    private User publisher;
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private String mCurrentUserId;
    private String mPublisherId;

    public PostAnsCrackItemOverflowListener(Context context, User publisher) {
        mContext = context;
        this.publisher = publisher;
    }

    @Override
    public void onClick(View view) {

        View bottomSheetView = View.inflate(mContext, R.layout.bottom_sheet_dialog_for_sharedposts, null);

        mDialog = new BottomSheetDialog(mContext);
        mDialog.setContentView(bottomSheetView);
        mDialog.show();

        inflateBottomSheetButtons(bottomSheetView);
    }

    private void inflateBottomSheetButtons(final View bottomSheetView) {

        followTView = bottomSheetView.findViewById(R.id.followTView);
        followBtnLayout = bottomSheetView.findViewById(R.id.followUser);
        deleteBtnLayout = bottomSheetView.findViewById(R.id.deleteBtn);
        editBtnLayout = bottomSheetView.findViewById(R.id.editNameBtn);
        copyBtnLayout = bottomSheetView.findViewById(R.id.copyBtn);
        notifBtnLayout =  bottomSheetView.findViewById(R.id.notifBtn);
        connectBtnLayout =  bottomSheetView.findViewById(R.id.connectBtn);


        followBtnLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.dismiss();

                final Map<String, Object> user = new HashMap<>();
                user.put("firstName", publisher.getFirstName());
                user.put("dpUrl", publisher.getDpUrl());
                mPublisherId = publisher.getUserId();
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
        });

    }
}
