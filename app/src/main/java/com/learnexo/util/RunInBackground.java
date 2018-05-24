package com.learnexo.util;

import android.os.AsyncTask;

import com.learnexo.dao.PostDao;

public class RunInBackground extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        long l= PostDao.getNumberOfUpVotes((String) objects[0], (String) objects[1]);
        return  l;
    }
}
