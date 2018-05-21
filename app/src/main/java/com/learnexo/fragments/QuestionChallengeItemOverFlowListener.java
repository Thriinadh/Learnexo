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

public class QuestionChallengeItemOverFlowListener implements View.OnClickListener {

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

    public QuestionChallengeItemOverFlowListener(Context context, User publisher) {
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

                final Map<String, Object> followingUser = new HashMap<>();
                followingUser.put("name", publisher.getFirstName());
                followingUser.put("dpUrl", publisher.getDpUrl());

                mFirebaseUtil.mFirestore.collection("users").document(FirebaseUtil.getCurrentUserId())
                        .collection("following").document(publisher.getUserId()).set(followingUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                followingUser.put("name", FeedFragment.sName);
                                followingUser.put("dpUrl", FeedFragment.sDpUrl);
                                mFirebaseUtil.mFirestore.collection("users").document(publisher.getUserId())
                                        .collection("followers").document(FirebaseUtil.getCurrentUserId()).set(followingUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        followTView.setVisibility(View.INVISIBLE);

                                        Toast.makeText(mContext, "Now You are following "+publisher.getFirstName(), Toast.LENGTH_LONG).show();

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
