package com.example.jandroid.booklisting;

import android.graphics.drawable.Drawable;

public class Book {
    private String mTtitle;
    private String mAuthors;
    private int mPageCount;
    private String mSale;
    private String mPic;

    public Book(String title, String authors,int pageCount,String sale,String pic){
        mTtitle=title;
        mAuthors=authors;
        mPageCount=pageCount;
        mSale=sale;
        mPic=pic;

    }
    public Book(String title, String authors,int pageCount,String sale){
        mTtitle=title;
        mAuthors=authors;
        mPageCount=pageCount;
        mSale=sale;


    }

    public String getmTtitle() {
        return mTtitle;
    }

    public void setmTtitle(String mTtitle) {
        this.mTtitle = mTtitle;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public void setmAuthors(String mAuthors) {
        this.mAuthors = mAuthors;
    }



    public int getmPageCount() {
        return mPageCount;
    }

    public void setmPageCount(int mPageCount) {
        this.mPageCount = mPageCount;
    }

    public String getmSale() {
        return mSale;
    }

    public void setmSale(String mSale) {
        this.mSale = mSale;
    }

    public String getmPic() {
        return mPic;
    }

    public void setmPic(String mPic) {
        this.mPic = mPic;
    }
}
