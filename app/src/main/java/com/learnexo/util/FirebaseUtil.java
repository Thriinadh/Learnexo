package com.learnexo.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class FirebaseUtil {

    public static FirebaseAuth sAuth;
    public static StorageReference sStorageReference;


    public FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

     static {
        sAuth = FirebaseAuth.getInstance();
        sStorageReference = FirebaseStorage.getInstance().getReference();
    }

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





    private void saveImagetoStorage(final Uri uri, String path, final Activity activity) {
        final String randomName = UUID.randomUUID().toString();

        StorageReference filepath = FirebaseUtil.sStorageReference.child("answer_images").child(randomName + ".jpg");

        filepath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                final String downloadUri = task.getResult().getDownloadUrl().toString();
                Bitmap mBitmap=null;
                if (task.isSuccessful()) {
                    File newImageFile = new File(uri.getPath());
                    try {

                        mBitmap = new Compressor(activity)
                                .setMaxHeight(100)
                                .setMaxWidth(100)
                                .setQuality(2)
                                .compressToBitmap(newImageFile);
                    } catch (IOException e) {
                        Log.e("TAG", Arrays.toString(e.getStackTrace()));
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] thumbData = baos.toByteArray();

                    String thumbs= "answer_images".concat("/thumbs");
                    UploadTask uploadTask = FirebaseUtil.sStorageReference.child(thumbs)
                            .child(randomName + ".jpg").putBytes(thumbData);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();
//                            answer.setImgUrl(downloadUri);
//                            answer.setImgThmb(downloadthumbUri);
//                            saveAnswer(answer);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = task.getException().getMessage();
                            Toast.makeText(activity,
                                    "Firestore Retrieve Error : " + error, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    // mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
