package com.learnexo.model.feed.likediv;

import com.learnexo.model.core.BookMarkType;

import java.util.Date;

public class Bookmark {
    private String bookMarkItemId;
    private String publisherId;
    private String bookMarkerId;
    private BookMarkType mBookMarkType;
    private Date bookMarkTime;

    public String getBookMarkItemId() {
        return bookMarkItemId;
    }

    public void setBookMarkItemId(String bookMarkItemId) {
        this.bookMarkItemId = bookMarkItemId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getBookMarkerId() {
        return bookMarkerId;
    }

    public void setBookMarkerId(String bookMarkerId) {
        this.bookMarkerId = bookMarkerId;
    }

    public BookMarkType getBookMarkType() {
        return mBookMarkType;
    }

    public void setBookMarkType(BookMarkType bookMarkType) {
        mBookMarkType = bookMarkType;
    }

    public Date getBookMarkTime() {
        return bookMarkTime;
    }

    public void setBookMarkTime(Date bookMarkTime) {
        this.bookMarkTime = bookMarkTime;
    }
}
