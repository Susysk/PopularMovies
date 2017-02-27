package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.movies.Movie;

import java.util.ArrayList;

/**
 * Created by Susy on 25/02/2017.
 */

public class MovieFavoriteAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private static final String TAG = MovieFavoriteAsyncTask.class.getSimpleName();
    APISingleton singleton;
    private final ContentResolver contentResolver;
    public MovieFavoriteAsyncTask(ContentResolver res, Context c){
        this.contentResolver=res;
        singleton=APISingleton.getInstance(c);
    }
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        ArrayList<Movie> moviePosters = new ArrayList<>();
        String[] projection = new String[]{"id", "path"};
        final Cursor cursor = contentResolver.query(Uri.parse("content://com.example.android.popularmovies/favorites"),projection,null,null,null);
        Log.d(TAG,"Cursor count:"+cursor.getCount() );
        if (cursor.getCount()!=0) {
            Log.e(TAG,"cursor column count:"+cursor.getColumnCount());
            while(cursor.moveToNext()) {
                Movie data = new Movie(cursor.getString(1), cursor.getInt(0));
                moviePosters.add(data);
            }
        }
        return moviePosters;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> list) {
        super.onPostExecute(list);
        singleton.getImages().clear();
        singleton.getId().clear();
        if(list!=null) {
            for (Movie m : list) {
                singleton.getFavImages().put(m.getId(),m.getPosterPath());
            }
            MainActivity.movieAdapter.notifyDataSetChanged();
        }
    }
}