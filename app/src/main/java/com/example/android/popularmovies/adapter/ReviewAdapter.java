package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.movies.Review;

import java.util.List;

/**
 * Created by Susy on 23/02/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements View.OnClickListener {
    public static List<Review> reviews;
    private Context context=null;
    public ReviewAdapter(Context context,List<Review> articles){
        this.context=context;
        this.reviews =articles;

    }
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trailer = inflater.inflate(R.layout.review_element, parent, false);
        ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder(trailer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        TextView name = holder.name;
        TextView site = holder.site;
        Review review = reviews.get(position);
        name.setText(review.getAuthor());
        site.setText(review.getContent());
//        site.setText(reviews.get(position).getSite());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView name;
        public  TextView site;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.author);
            site = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
