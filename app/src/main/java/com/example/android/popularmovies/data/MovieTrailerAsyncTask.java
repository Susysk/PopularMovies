package com.example.android.popularmovies.data;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.movies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Susy on 18/02/2017.
 */

public class MovieTrailerAsyncTask extends AsyncTask<String, Void, String>{
    static APISingleton singleton;
    public static int id;

    public static TrailerAdapter getTrailerAdapter() {
        return trailerAdapter;
    }

    public static void setTrailerAdapter(TrailerAdapter trailerAdapter) {
        MovieTrailerAsyncTask.trailerAdapter = trailerAdapter;
    }

    public static TrailerAdapter trailerAdapter;
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    private Activity activity;

    @Override
    protected String doInBackground(String... params) {
        singleton = APISingleton.getInstance(activity);
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            final String VIDEOS = "videos";
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(String.valueOf(id))
                        .appendPath(VIDEOS)
                        .appendQueryParameter(API_KEY_PARAM, singleton.getApiKey())
                        .build();
        String response;
        try {
            response  = getConnection(builtUri);
            return response;
        }catch (Exception e){
//            if(activity!=null)
//                activity.startActivity(new Intent(activity, ErrorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            return null;
        }
    }

    public   void getTrailersDataFromJson(String moviesJsonStr){
        try {
            JSONObject moviesObject = new JSONObject(moviesJsonStr);
            Integer movieId = moviesObject.getInt("id");
            Movie movie = null;
            for (Movie m : singleton.getMovies()) {
                if (m.getId() == movieId)
                    movie = m;
            }
            movie.getTrailers().clear();
            JSONArray moviesArray = moviesObject.getJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject newMovie = moviesArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setId(newMovie.getString("id"));
                trailer.setIso_639_1(newMovie.getString("iso_639_1"));
                trailer.setKey(newMovie.getString("key"));
                trailer.setName(newMovie.getString("name"));
                trailer.setSite(newMovie.getString("site"));
                trailer.setSize(newMovie.getString("size"));
                movie.getTrailers().add(trailer);
            }
        }
        catch (JSONException e) {

        }
    }


    public  String getConnection(Uri builder) {
        InputStream inputStream;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String RESULT = null;

        try {
            URL url = new URL(builder.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) {
                Log.e("Error", "No data");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) {
                return null;
            }
            RESULT = stringBuffer.toString();
        } catch (MalformedURLException  e) {
            return null;
        } catch  (ProtocolException e){

        }catch(IOException e){

        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }
    getTrailersDataFromJson(RESULT);
        return RESULT;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(trailerAdapter!=null)
        trailerAdapter.notifyDataSetChanged();
        if (response != null) {
                getTrailersDataFromJson(response);
        }
//        else {
//            activity.startActivity(new Intent(activity, ErrorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//        }

    }
}