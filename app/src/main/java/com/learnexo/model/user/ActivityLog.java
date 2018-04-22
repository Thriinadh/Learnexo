package com.learnexo.model.user;

import com.learnexo.model.core.Bookmark;
import com.learnexo.model.core.Comment;
import com.learnexo.model.core.Like;
import com.learnexo.model.core.Share;

import java.util.List;

public class ActivityLog {
    private UserId userId;
    private List<Like> likeList;
    private List<Comment> commentList;
    private List<Share> shareList;
    private List<Bookmark> bookmarkList;


}
