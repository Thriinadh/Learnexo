package com.learnexo.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.learnexo.main.MainActivity;
import com.learnexo.main.R;

public class FirebaseUtil {

    public static FirebaseAuth sAuth;
    public static StorageReference sStorageReference;


    public FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    {
        getDPandUserName();
    }

     static {
        sAuth = FirebaseAuth.getInstance();
        sStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private String mUserId;
    public static String sName;
    public static String sDpUrl;

    public static boolean doesUserExist() {
        return sAuth.getCurrentUser() != null;
    }

    public static String getCurrentUserId() {
         FirebaseUser user= sAuth.getCurrentUser();
        if (user == null) {
            return "";
        } else {
            return user.getUid();
        }
    }

    public static FirebaseUser getCurrentUser() {
        return sAuth.getCurrentUser();
    }

    public static void sAuthSingOut() {
        sAuth.signOut();
    }

    public void getDPandUserName() {
        mUserId = FirebaseUtil.getCurrentUserId();
        mFirestore.collection("users").document(mUserId).
                collection("reg_details").document("doc").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()) {
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");

                        sName =firstName.concat(" "+lastName);

                        sDpUrl = (null== MainActivity.photoUrl)? snapshot
                                .getString("dpUrl"):MainActivity.photoUrl;
                    }
                } else {
                    String error = task.getException().getMessage();
                    Log.e("FirebaseUitl",error);

                }

            }
        });
    }
}
