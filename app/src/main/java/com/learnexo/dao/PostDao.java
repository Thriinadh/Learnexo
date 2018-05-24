package com.learnexo.dao;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.learnexo.model.feed.post.Post;
import com.learnexo.model.feed.post.PostDetails;
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


    public static PostDetails getNumberOfUpVotes(String publisherId, String postId){

        final Task<DocumentSnapshot> documentSnapshotTask = mFirebaseUtil.mFirestore.collection("users").
                document(publisherId).collection("posts").document(postId).get();
        final Object[] upVotes = new Object[2];

                    try {
                        DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);
                        upVotes[0] = documentSnapshot.get("upVotes");
                        upVotes[1] = documentSnapshot.get("views");

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    PostDetails postDetails=new PostDetails();
                    postDetails.setNoOfLikes((Long) upVotes[0]);
                    postDetails.setNoOfViews((Long) upVotes[1]);

        return postDetails;
    }


}
