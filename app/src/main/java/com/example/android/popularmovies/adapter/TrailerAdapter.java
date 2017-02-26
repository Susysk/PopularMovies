package com.example.android.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.movies.Trailer;

import java.util.List;

/**
 * Created by Susy on 18/02/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> implements View.OnClickListener {
    public static List<Trailer> trailers;
    private Context context=null;
    public TrailerAdapter(Context context,List<Trailer> articles){
        this.context=context;
        this.trailers=articles;

    }
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View trailer = inflater.inflate(R.layout.trailer_element, parent, false);
        TrailerAdapter.ViewHolder viewHolder = new ViewHolder(trailer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        final String id = trailers.get(position).getKey();
        TextView name = holder.name;
        TextView site = holder.site;
        name.setText(((Trailer)trailers.get(position)).getName());
        System.out.println(trailers.get(position).getSite());
//        site.setText(reviews.get(position).getSite());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView name;
        public  TextView site;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            site = (TextView) itemView.findViewById(R.id.site);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i= getAdapterPosition();
                    Trailer t =trailers.get(i);
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + t.getKey()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + t.getKey()));
                    try {
                        v.getContext().startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        v.getContext(). startActivity(webIntent);
                    }
                }
            });
        }
    }
}
