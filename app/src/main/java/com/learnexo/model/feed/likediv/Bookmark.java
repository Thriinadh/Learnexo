package com.learnexo.model.feed.likediv;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import com.learnexo.model.core.BookMarkType;

import java.util.Date;

//////////// over ride equals and hashcode //////////////

@IgnoreExtraProperties
public class Bookmark {
    private String bookMarkItemId;
    private String publisherId;
    private String bookMarkerId;
    private BookMarkType bookMarkType;

    @ServerTimestamp
    private Date bookMarkTime;
    public Bookmark(){}
    public Bookmark(String bookMarkItemId, String publisherId, BookMarkType bookMarkType) {
        this.bookMarkItemId = bookMarkItemId;
        this.publisherId = publisherId;
        this.bookMarkType = bookMarkType;
    }
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
        return bookMarkType;
    }

    public void setBookMarkType(BookMarkType bookMarkType) {
        this.bookMarkType = bookMarkType;
    }

    public Date getBookMarkTime() {
        return bookMarkTime;
    }

    public void setBookMarkTime(Date bookMarkTime) {
        this.bookMarkTime = bookMarkTime;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Bookmark)) {
            return false;
        }

        Bookmark bookmark = (Bookmark) o;

        return bookmark.bookMarkItemId.equals(bookMarkItemId) &&
                bookmark.bookMarkerId.equals(bookMarkerId) &&
                bookmark.bookMarkType.name().equals(bookMarkType.name()) &&
                bookmark.publisherId.equals(publisherId);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + bookMarkItemId.hashCode();
        result = 31 * result + bookMarkerId.hashCode();
        result = 31 * result + bookMarkType.name().hashCode();
        result = 31 * result + publisherId.hashCode();
        return result;
    }
}
