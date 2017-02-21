package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.host.APISingleton;
import com.example.android.popularmovies.host.MovieTrailerAsyncTask;
import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.movies.Trailer;
import com.example.android.popularmovies.movies.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailsActivityFragment extends Fragment implements AdapterView.OnItemClickListener{
    APISingleton singleton;
    DetailsActivity activity;
    public static TrailerAdapter adapter;
    Movie movie;
    MovieTrailerAsyncTask task;
    public DetailsActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_activity, container, false);
        setHasOptionsMenu(true);
        singleton = APISingleton.getInstance(getContext());
        activity = (DetailsActivity) getActivity();
        TextView movie_release;
        TextView movie_title;
        TextView movie_year;
        TextView movie_overview;
        TextView movie_rate;
        RatingBar rating;
        ImageView movie_poster;
        RecyclerView trailers;
        movie = singleton.movies.get(((DetailsActivity)getActivity()).movie_Position);
        movie_title = (TextView) rootView.findViewById(R.id.movie_title);
        movie_rate = (TextView) rootView.findViewById(R.id.movie_rating);
        movie_poster = (ImageView) rootView.findViewById(R.id.movie_poster);
        movie_overview = (TextView) rootView.findViewById(R.id.movie_overview);
        movie_year = (TextView) rootView.findViewById(R.id.movie_year);
        trailers = (  RecyclerView)  rootView.findViewById(R.id.trailers);
        trailers.setVisibility(View.VISIBLE);
        if(movie.getTrailers()!=null) {
            ArrayList<Trailer> prova = new ArrayList<>();
            adapter = new TrailerAdapter(getContext(),movie.getTrailers());
            activity.trailerAdapter=adapter;
            trailers.setAdapter(adapter);
            trailers.setLayoutManager(new GridLayoutManager(getContext(),1));
        }
        try {
            movie_title.setText(movie.getTitle());
            movie_year.setText(movie.getRelease());
            movie_title.setVisibility(View.VISIBLE);
            movie_rate.setText(movie.getVote_average() + "/10");
            movie_overview.setText(movie.getOverview());
            rating = (RatingBar)rootView.findViewById(R.id.rating);
            if (Double.parseDouble(movie.getVote_average()) <= 2.0) {
                rating.setNumStars(1);
            }
            if (Double.parseDouble(movie.getVote_average()) <= 4.0 && Double.parseDouble(movie.getVote_average()) > 2.0) {
                rating.setNumStars(2);
            }
            if (Double.parseDouble(movie.getVote_average()) <= 6.0 && Double.parseDouble(movie.getVote_average()) > 4.0) {
                rating.setNumStars(3);
            }
            if (Double.parseDouble(movie.getVote_average()) <= 8.0 && Double.parseDouble(movie.getVote_average()) > 6.0) {
                rating.setNumStars(4);
            }
            if (Double.parseDouble(movie.getVote_average()) <= 10.0 && Double.parseDouble(movie.getVote_average()) > 8.0) {
                rating.setNumStars(5);
            }
            String movie_poster_url;
            if (movie.getPosterPath().equals(APISingleton.getImageNotFound())) {
                movie_poster_url = APISingleton.getImageUrl();
            } else {
                movie_poster_url = APISingleton.getImageUrl() + APISingleton.getImageSize() + "/" + movie.getPosterPath();
            }
            Picasso.with(getActivity()).load(movie_poster_url).into(movie_poster);
            movie_poster.setVisibility(View.VISIBLE);
        }
        catch(Exception e){

        }
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
            updateMovies();
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        if(task!=null)
         task.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
            updateMovies();
    }
    public void updateMovies() {
       task = new MovieTrailerAsyncTask();
        task.setActivity(getActivity());
        task.setId(((DetailsActivity)getActivity()).idMovie);
        task.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trailer trailer = (Trailer)movie.getTrailers().get(position);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "opkxU0dXMww"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
            getActivity().finish();
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
            getActivity().finish();
        }
    }
    }

