package com.learnexo.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import id.zelory.compressor.Compressor;

public class FirebaseUtil {

    public static FirebaseAuth sAuth;
    public static StorageReference sStorageReference;
    private  static final String TAG=FirebaseUtil.class.getSimpleName();


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




    public void saveInterestFeedItem(FeedItem mFeedItem, Task<DocumentReference> documentReferenceTask, String interestFeedPath, int type, String publisherId) {
        InterestFeed interestFeed=new InterestFeed();

        interestFeed.setInterest(mFeedItem.getTags().get(0));
        interestFeed.setPublisherId(publisherId);
        interestFeed.setFeedType(type);
        interestFeed.setFeedItemId(documentReferenceTask.getResult().getId());

        mFirestore.collection(interestFeedPath).add(interestFeed);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ImageUtil saveImagetoStorage(final Uri mPublishedImageUri, final String path, final Activity activity, final ProgressBar mProgressBar) {

        final CompletableFuture<ImageUtil> futureImageUtil = new CompletableFuture<>();;

        final ImageUtil imageUtil = new ImageUtil();

        final String randomName = UUID.randomUUID().toString();
        StorageReference filepath = sStorageReference.child(path).child(randomName + ".jpg");

        filepath.putFile(mPublishedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                final String downloadUri = task.getResult().getDownloadUrl().toString();

                if (task.isSuccessful()) {
                    File newImageFile = new File(mPublishedImageUri.getPath());
                    Bitmap mBitmap=null;
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

                    String thumbs= path.concat("/thumbs");
                    UploadTask uploadTask = FirebaseUtil.sStorageReference.child(thumbs)
                            .child(randomName + ".jpg").putBytes(thumbData);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();
                            imageUtil.setImgUrl(downloadUri);
                            imageUtil.setImgThumb(downloadthumbUri);
                            futureImageUtil.complete(imageUtil);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = task.getException().getMessage();
                            futureImageUtil.complete(null);
                            Log.e(TAG,"Firestore Retrieve Error : " + error);
                        }
                    });


                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        try {
            return futureImageUtil.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
     }

}
