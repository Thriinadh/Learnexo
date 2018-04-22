package com.learnexo.model.core;

import com.learnexo.model.user.UserId;

import java.util.Date;
import java.util.List;

public class Post {
    private UserId userId;
    private PostId postId;
    private Date postedDate;
    private PostContent postContent;
    private List<String> imageUrlListl;
    private List<Tag> tagList;
}
