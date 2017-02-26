package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.APISingleton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends BaseAdapter {

    public static APISingleton singleton;
    private Context context;

    public static int getFav() {
        return fav;
    }

    public static void setFav(int fav) {
        MovieAdapter.fav = fav;
    }

    static int fav=0;

    public MovieAdapter(Context c) {
        context = c;
        singleton = APISingleton.getInstance(c);
    }
    public int getCount() {
        if(fav==0)
        return singleton.getImages().size();
        else
            return singleton.getFavImages().size();
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
        if(fav==1 && singleton.getFavImages().isEmpty()==false){
            ArrayList<String> lista=new ArrayList<>();
            for(String s: singleton.getFavImages().values()) {
                lista.add(s);
                Log.i("ciao",lista.get(0));

            }
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w342"+lista.get(position))
                    .placeholder(R.drawable.trex)
                    .into(poster);
        }
        else {
            Picasso.with(context)
                    .load(singleton.images.get(position))
                    .placeholder(R.drawable.trex)
                    .error(R.drawable.trex)
                    .into(poster);
        }
        return poster;
    }

}

