package com.example.android.popularmovies.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.host.APISingleton;
import com.squareup.picasso.Picasso;


public class MovieAdapter extends BaseAdapter {

    public static APISingleton singleton;
    private Context context;


    public MovieAdapter(Context c) {
        context = c;
        singleton = APISingleton.getInstance(c);
    }
    public int getCount() {
        return singleton.getImages().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView poster;
       if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            poster = (ImageView) inflater.inflate(R.layout.movie, parent, false);
       } else {
            poster = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(singleton.images.get(position))
                .into(poster);
        return poster;
    }

}

