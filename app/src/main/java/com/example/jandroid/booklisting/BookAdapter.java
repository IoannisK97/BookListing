package com.example.jandroid.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private List<Book> books;
    private Context context;

    public BookAdapter(@NonNull Context context, List<Book> books) {
        this.context=context;
        this.books=books;
    }

    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book tempBook=books.get(position);
        holder.title.setText(tempBook.getmTtitle());
        if (tempBook.getmAuthors()!=null && !tempBook.getmAuthors().isEmpty()){
            holder.author.setText(tempBook.getmAuthors());
        }
        else {
            holder.author.setText("No author data avaialable");
        }
        if (tempBook.getmPageCount()==0){
            holder.pageCount.setText("Number of pages not available");
        }
        else {
            holder.pageCount.setText("Number of pages " + tempBook.getmPageCount());
        }
        holder.saleability.setText("For sale; "+tempBook.getmSale());


        if (tempBook.getmPic()!= null){
            Glide.with(context).load(tempBook.getmPic()).centerCrop().into(holder.thumbnail);

        }



    }


    @Override
    public int getItemCount() {
        return books.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, author, pageCount, saleability;
        public ImageView thumbnail;
        public MyViewHolder(View view){
            super(view);
            title=(TextView) view.findViewById(R.id.title);
            author=(TextView) view.findViewById(R.id.author);
            pageCount=(TextView) view.findViewById(R.id.count);
            saleability=(TextView) view.findViewById(R.id.sale);
            thumbnail=(ImageView) view.findViewById(R.id.thumbnail);

        }
    }

    public void clear(){
        int size=books.size();
        books.clear();
        notifyItemRangeRemoved(0,size);
    }



}
