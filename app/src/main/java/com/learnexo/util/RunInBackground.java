package com.learnexo.util;

import android.os.AsyncTask;

import com.learnexo.dao.PostDao;
import com.learnexo.model.feed.post.PostDetails;

public class RunInBackground extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        PostDetails postDetails = PostDao.getNumberOfUpVotes((String) objects[0], (String) objects[1]);
        return  postDetails;
    }
}
