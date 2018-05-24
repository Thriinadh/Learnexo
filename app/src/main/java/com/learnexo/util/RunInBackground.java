package com.learnexo.util;

import android.os.AsyncTask;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.model.feed.post.PostDetails;

import java.util.concurrent.ExecutionException;


public class RunInBackground extends AsyncTask {

FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    @Override
    protected Object doInBackground(Object[] objects) {
        Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").
                document((String) objects[0]).collection("posts").document((String) objects[1]).get();
        PostDetails postDetails=null;

        try {
            DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);

            postDetails = new PostDetails();
            postDetails.setNoOfLikes((Long) documentSnapshot.get("upVotes"));
            postDetails.setNoOfViews((Long) documentSnapshot.get("views"));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return postDetails;
    }
}
