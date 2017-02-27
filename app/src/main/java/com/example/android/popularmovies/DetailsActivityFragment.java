package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.data.APISingleton;
import com.example.android.popularmovies.data.DBHelper;
import com.example.android.popularmovies.data.MovieReviewAsyncTask;
import com.example.android.popularmovies.data.MovieTrailerAsyncTask;
import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.movies.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailsActivityFragment extends Fragment {
    APISingleton singleton;
    DetailsActivity activity;
    MainActivity activityTablet;
    public static TrailerAdapter trailerAdapter;
    public static ReviewAdapter reviewAdapter;
    Movie movie;
    boolean favorite =false;
    MovieTrailerAsyncTask task;
    @BindView(R.id.movie_title)  TextView movie_title;
    @BindView(R.id.movie_year) TextView movie_year;
            @BindView(R.id.reviewsTitle)TextView reviewsTitle;
    @BindView(R.id.trailersTitle)TextView trailerTitle;
    @BindView(R.id.movie_overview) TextView movie_overview;
    @BindView(R.id.movie_rating)TextView movie_rate;
    @BindView(R.id.rating) RatingBar rating;
    @BindView(R.id.movie_poster)ImageView movie_poster;
    @BindView(R.id.trailers)RecyclerView trailers;
    @BindView(R.id.reviews)RecyclerView reviews;
    MovieReviewAsyncTask reviewAsyncTask;
    private Unbinder unbinder;

    public DetailsActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle onSavedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_activity, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, rootView);
        DBHelper db = new DBHelper(getContext());
        singleton = APISingleton.getInstance(getContext());

        for(Movie m: singleton.movies){
            if(m.getId()==singleton.trailerIn)
                movie=m;
        }
        final Button favorites;
        favorites = (Button) rootView.findViewById(R.id.favorites);
        if(movie!=null) {
            trailerTitle = (TextView) rootView.findViewById(R.id.trailersTitle);
            trailerTitle.setVisibility(View.VISIBLE);
            reviewsTitle = (TextView) rootView.findViewById(R.id.reviewsTitle);
            reviewsTitle.setVisibility(View.VISIBLE);
        }
        trailers.setVisibility(View.VISIBLE);
        reviews.setVisibility(View.VISIBLE);
        if(movie !=null) {
            Map<Integer,String> favoritesL = db.findall();
            if(favoritesL.containsKey(movie.getId())){
                favorite=true;
            }
            Uri movieUri = Uri.parse("content://com.example.android.popularmovies/favorites" + "/" + movie.getId());
            String[] projection = new String[]{"id"};
            Cursor cursor = getActivity().getContentResolver().query(movieUri, projection, null, null, null);
                if(favorite==true)
                favorites.setText("Delete from favorites");
            else
                    favorites.setText("Add to favorites");
            if (movie.getTrailers() != null) {
                trailerAdapter = new TrailerAdapter(getContext(), movie.getTrailers());
                trailers.setAdapter(trailerAdapter);
                trailers.setLayoutManager(new GridLayoutManager(getContext(), 1));
            }favorites.setVisibility(View.VISIBLE);
            favorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (favorite == false) {
                        ContentValues values = new ContentValues();
                        values.put("id", movie.getId());
                        values.put("path", movie.getPosterPath());
                        Log.i("ciao", movie.getPosterPath());
                        values.put("title", movie.getTitle());
                        values.put("release", movie.getRelease());
                        favorites.setText("Delete from favorites");
                        favorite = true;
                        values.put("vote_average", movie.getVote_average());
                        Uri uri = getActivity().getContentResolver().insert(Uri.parse("content://com.example.android.popularmovies/favorites"), values);
                    } else {
                        favorites.setText("Add to favorites");
                        getActivity().getContentResolver().delete(Uri.parse("content://com.example.android.popularmovies/favorites"), null, null);
                        favorite = false;
                    }
                }
            });
            if (movie.getReviews() == null)
                movie.setReviews(new ArrayList<Review>());
            reviewAdapter = new ReviewAdapter(getContext(), movie.getReviews());
            reviews.setAdapter(reviewAdapter);
            reviews.setLayoutManager(new GridLayoutManager(getContext(), 1));
            try {
                movie_title.setText(movie.getTitle());
                movie_year.setText(movie.getRelease());
                movie_title.setVisibility(View.VISIBLE);
                movie_rate.setText(movie.getVote_average() + "/10");
                movie_overview.setText(movie.getOverview());
                rating = (RatingBar) rootView.findViewById(R.id.rating);
                rating.setVisibility(View.VISIBLE);
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
            } catch (Exception e) {

            }
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
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        if(task!=null)
         task.cancel(true);
        if(reviewAsyncTask!=null)
            reviewAsyncTask.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
            updateMovies();
    }
    public void updateMovies() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                reviewAsyncTask = new MovieReviewAsyncTask();
                reviewAsyncTask.setActivity(getActivity());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                int id = preferences.getInt("id",0);
                reviewAsyncTask.setId(singleton.trailerIn);
                reviewAsyncTask.setReviewAdapter(reviewAdapter);
                reviewAsyncTask.execute();
                task = new MovieTrailerAsyncTask();
                task.setActivity(getActivity());
                task.setId((singleton.trailerIn));
                task.setTrailerAdapter(trailerAdapter);
                task.execute();
            }
        });
        thread.start();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
    }
}



