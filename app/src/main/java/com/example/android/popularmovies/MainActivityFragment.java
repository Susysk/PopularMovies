package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmovies.host.APISingleton;
import com.example.android.popularmovies.host.MovieAsyncTask;
import com.example.android.popularmovies.host.MovieTrailerAsyncTask;
import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.movies.MovieAdapter;

import java.util.ArrayList;

/**
 * Created by Susy on 06/02/2017.
 */

public class MainActivityFragment extends Fragment {
    APISingleton singleton;
    MainActivity activity;
    MovieTrailerAsyncTask trailerAsyncTask;
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        singleton = APISingleton.getInstance(getContext());
      activity = (MainActivity) getActivity();
       activity.gridview = (GridView) rootView.findViewById(R.id.grid);
        activity.gridview.setNumColumns(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
        activity.gridview.setAdapter(activity.movieAdapter);
        activity.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                MainActivity activity =(MainActivity)getActivity();
                intent.putExtra("position", position);
                startActivity(intent);
                try {
                    activity.unregisterReceiver(activity.getReceiver());
                }
                catch(Exception e){

                }
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies",(ArrayList)singleton.getMovies());
        outState.putStringArrayList("images", (ArrayList)singleton.getImages());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey("movies")) {
            singleton.images = savedInstanceState.getStringArrayList("images");
            singleton.movies = savedInstanceState.getParcelableArrayList("movies");
        }else{
            singleton.setImages(new ArrayList<String>());
            singleton.setMovies(new ArrayList<Movie>());
            activity.movieAdapter = new MovieAdapter(getActivity());
            updateMovies();
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingCriteria = sharedPrefs.getString("criteria key", "popularity");
        super.onResume();
        if(activity.lastCriteria != null && !sortingCriteria.equals(activity.lastCriteria)){
            singleton.setMovies(new ArrayList<Movie>());
            singleton.setImages(new ArrayList<String>());
            updateMovies();
        }
        activity.lastCriteria = sortingCriteria;
    }
    public void updateMovies() {
        MovieAsyncTask task = new MovieAsyncTask();
        task.setActivity(getActivity());
        task.execute(getResources().getString(R.string.popular), null);
        trailerAsyncTask = new MovieTrailerAsyncTask();
       trailerAsyncTask.setActivity(getActivity());
    }

}
