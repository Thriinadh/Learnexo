package com.learnexo.dao;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.model.feed.post.Post;
import com.learnexo.util.FirebaseUtil;

import java.util.concurrent.ExecutionException;

public class PostDao {
    private static FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private Post mPost;

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }


    public static long getNumberOfUpVotes(String publisherId, String postId){

        final Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").
                document(publisherId).collection("posts").document(postId).get();
        final Object[] upVotes = new Object[1];

                    try {
                        DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);
                        upVotes[0] = documentSnapshot.get("upVotes");

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

        return (Long) upVotes[0];
    }


}
