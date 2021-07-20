package com.example.jandroid.booklisting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private String REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    private RecyclerView recyclerView;
    private BookAdapter mAdapter;
    private TextView mEmptyStateView;
    private ProgressBar mProgressSpinner;
    private ArrayList<Book> books=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        recyclerView=(RecyclerView) findViewById(R.id.recycler);
        mEmptyStateView=(TextView) findViewById(R.id.empty_view);
        mProgressSpinner= (ProgressBar) findViewById(R.id.progress_bar);

        Intent queryIntent = getIntent();
        String searchText = queryIntent.getStringExtra("topic");

        String processedQuery = searchText;
        
        REQUEST_URL += processedQuery + "&maxResults=40" ;

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, BookActivity.this);
        } else {

            mProgressSpinner.setVisibility(View.GONE);
            mEmptyStateView.setText("No internet connecton");

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BookAdapter(this,books));
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(BookActivity.this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (books!=null){
            mProgressSpinner.setVisibility(View.GONE);
            mEmptyStateView.setText("Empty");
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new BookAdapter(this,books));
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}