package com.learnexo.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

public class PostDao {
    private FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private Post mPost;

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }

    public long getNumberOfUpVotes(String publisherId, String postId){

        Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").
                document(publisherId).collection("posts").document(postId).get();

        documentSnapshotTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Object upVotes = documentSnapshot.get("upVotes");

                }
            }
        });
//        documentSnapshotTask
        return 0;
    }


}
