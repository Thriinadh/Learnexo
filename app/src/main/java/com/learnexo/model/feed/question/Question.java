package com.learnexo.model.feed.question;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.learnexo.model.feed.FeedItem;

//only used for unanswered questions
@IgnoreExtraProperties
public class Question extends FeedItem{
    private boolean isChallenge;

    public Question() {}

    public Question(boolean isChallenge) {
        this.isChallenge = isChallenge;
    }

    public boolean isChallenge() {
        return isChallenge;
    }
    public void setChallenge(boolean challenge) {
        isChallenge = challenge;
    }
}
