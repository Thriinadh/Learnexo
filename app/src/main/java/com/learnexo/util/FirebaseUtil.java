package com.learnexo.util;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.learnexo.main.PublishActivity;
import com.learnexo.model.feed.FeedItem;
import com.learnexo.model.feed.InterestFeed;
import com.learnexo.model.user.User;

import java.util.ArrayList;
import java.util.List;

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




    public void saveInterestFeedItem(FeedItem mFeedItem, Task<DocumentReference> documentReferenceTask, String interestFeedPath) {
        InterestFeed interestFeed = new InterestFeed();

        interestFeed.setInterest(mFeedItem.getTags().get(0));
        interestFeed.setPublisherId(mFeedItem.getUserId());
        interestFeed.setFeedType(mFeedItem.getType());
        interestFeed.setFeedItemId(documentReferenceTask.getResult().getId());

        mFirestore.collection(interestFeedPath).add(interestFeed);
    }

    public void pushFeedToFriends(List<User> friends, FeedItem feedItem, Task<DocumentReference> documentReferenceTask,
                                  String feedInboxPath) {

        feedItem.setFeedItemId(documentReferenceTask.getResult().getId());

        for(User friend : friends){
            String friendId = friend.getUserId();
            int edgeRank = generateEdgeRank(friend);
            feedItem.setEdgeRank(edgeRank);

            mFirestore.collection("users").document(friendId).collection(feedInboxPath).add(feedItem);
        }

    }


    public int generateEdgeRank(User friend) {
        //∑e = ue we de,
        //
        //∑e is the sum of the edge's rank,
        //ue is the affinity score with the user who created the edge,
        //we is the weight for the content type, and
        //de is a time decay factor.


        //        int effinityScore=generateEffinityScore(friend);


        return 10;
    }

    public int generateEffinityScore(User friend) {
        //∑i = li ni wi
        //
        //∑i is the sum of the interactions with that friend,
        //li is the time since your last interaction (weighted so that 1 day > 30 days),
        //ni is the number of interacts, and
        //wi is the weight of those interactions.

        return 5;
    }



    public class FriendsFetcher extends AsyncTask<Object, Void,List<User>> {

        @Override
        protected List<User> doInBackground(Object[] arguments) {

            final List<User> users=new ArrayList<>();
            if(arguments[1].getClass()==PublishActivity.class)


            mFirestore.collection("users").document((String) arguments[0]).collection("following").
                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    for(DocumentSnapshot documentSnapshot:documents){
                        User user = documentSnapshot.toObject(User.class);
                        users.add(user);
                    }

                }
            });

            return users;
        }

        @Override
        protected void onPostExecute(List<User> users) {
        }

    }

    public class RemoveFriends extends AsyncTask<Object, Object,List<User>> {

        @Override
        protected List<User> doInBackground(Object[] objects) {

            final List<User> friends= (List<User>) objects[0];
            final int i=100;
            for(User friend:friends){
                if(friend.getFollowers()>i){
                    friends.remove(friend);
                }
            }
            return friends;
        }

        @Override
        protected void onPostExecute(List<User> friends) {
        }

    }

}
