package com.example.android.popularmovies.host;

import android.app.Activity;
import android.content.Context;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.movies.Movie;

import java.util.ArrayList;
import java.util.List;

public class APISingleton {

    private  static APISingleton instance;
    public  static ArrayList<String> images;
    public  static String API_URL;
    public  static String API_KEY;
    public  static String IMAGE_URL;
    public  static String IMAGE_SIZE;
    public  static String IMAGE_NOT_FOUND;
    public static ArrayList<Integer> id;

    public static int getTrailerIn() {
        return trailerIn;
    }

    public static void setTrailerIn(int trailerIn) {
        APISingleton.trailerIn = trailerIn;
    }

    public static int trailerIn;

    public static ArrayList<Integer> getId() {
        return id;
    }

    public static void setId(ArrayList<Integer> id) {
        APISingleton.id = id;
    }


    public Activity getActivity() {
        return activity;
    }

    public static List<Movie> getMovies() {
        return movies;
    }
    public static void setMovies(List<Movie> movies) {
        APISingleton.movies = movies;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public static Activity activity;
    public  static List<Movie> movies;

    public static List<String> getImages() {
        return images;
    }

    public static void setImages(ArrayList<String> images) {
        APISingleton.images = images;
    }

     public static synchronized APISingleton getInstance(Context context) {
        if (instance == null)
            instance = new APISingleton();
         if(id==null)
             id = new ArrayList<>();
        return instance;

    }
    public static String getApiKey() {
        return activity.getResources().getString(R.string.movie_api);
    }
    public static String getImageSize() {
        return activity.getResources().getString(R.string.image_size);
    }

    public static String getApiUrl() {
        return activity.getResources().getString(R.string.movie_url);
    }
    public static String getImageUrl() {
        return activity.getResources().getString(R.string.image_url);
    }
    public static String getImageNotFound() {
        return activity.getResources().getString(R.string.image_not_found);
    }
}
