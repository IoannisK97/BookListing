package com.example.jandroid.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mSearchUrl;
    private List<Book> books;

    BookLoader(Context context, String url) {
        super(context);
        mSearchUrl = url;
    }

    @Override
    protected void onStartLoading() {
        if (books!= null) {
            deliverResult(books);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Book> loadInBackground() {
        if (mSearchUrl == null) {
            return null;
        }
        return QueryUtils.fetchBooks(mSearchUrl);
    }

    @Override
    public void deliverResult(List<Book> data) {
        books = data;
        super.deliverResult(data);
    }
}
