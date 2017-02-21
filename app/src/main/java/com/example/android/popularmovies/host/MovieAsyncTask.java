package com.example.android.popularmovies.host;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.example.android.popularmovies.ErrorActivity;
import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.movies.Movie;
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
public class MovieAsyncTask extends AsyncTask<String, Void, String> {
    private static APISingleton singleton;
    private Activity activity;

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String param = params[0];
        singleton = APISingleton.getInstance(activity);
        String response;
        Uri.Builder buildUpon = Uri.parse(singleton.getApiUrl()+param
                +"?api_key="+singleton.getApiKey()).buildUpon();
        try {
            response  = getConnection(buildUpon);
            return response;
        }catch (Exception e){
            if(activity!=null)
                activity.startActivity(new Intent(activity, ErrorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            return null;
        }
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            getMovies(response);
        } else {
            activity.startActivity(new Intent(activity, ErrorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        }

    }

    public  static void getMovies(String jsonString) {
        singleton.getImages().clear();
        singleton.getMovies().clear();
        singleton.getId().clear();

        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    Movie newMovie = new Movie();
                    newMovie.setTitle(movie.getString("title"));
                    newMovie.setId(movie.getInt("id"));
                    newMovie.setPath(movie.getString("backdrop_path"));
                    newMovie.setOriginal_language(movie.getString("original_language"));
                    newMovie.setOverview(movie.getString("overview"));
                    newMovie.setRelease(movie.getString("release_date"));
                    newMovie.setPopularity(movie.getString("popularity"));
                    newMovie.setVote_average(movie.getString("vote_average"));
                    newMovie.setPosterPath(movie.getString("poster_path"));
                    if (movie.getString("poster_path").equals("null")) {
                        singleton.images.add(APISingleton.getImageNotFound());
                        newMovie.setPosterPath(APISingleton.getImageNotFound());
                    } else {
                        singleton.images.add(APISingleton.getImageUrl()+ APISingleton.getImageSize() + movie.getString("poster_path"));
                    }
                    singleton.movies.add(newMovie);
                    singleton.getId().add(newMovie.getId());
                    MainActivity.movieAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String getConnection(Uri.Builder builder) {
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

        return RESULT;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}