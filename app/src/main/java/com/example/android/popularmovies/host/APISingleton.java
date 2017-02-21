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
//    public void getTrailer(){
//        String response;
//        for(int i:id) {
//            Uri.Builder buildUpon = Uri.parse(getApiUrl() +i + "?api_key=" + getApiKey()).buildUpon();
//            try {
//                response = getConnection(buildUpon);
//            } catch (Exception e) {
//                if (activity != null)
//                    activity.startActivity(new Intent(activity, ErrorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
//            }
//        }
//    }
//    public static String getConnection(Uri.Builder builder)
//    {
//        InputStream inputStream;
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String RESULT = null;
//
//        try {
//            URL url = new URL(builder.toString());
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            inputStream = urlConnection.getInputStream();
//            StringBuffer stringBuffer = new StringBuffer();
//            if (inputStream == null) {
//                Log.e("Error", "No data");
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                stringBuffer.append(line + "\n");
//            }
//            if (stringBuffer.length() == 0) {
//                return null;
//            }
//            RESULT = stringBuffer.toString();
//        } catch (MalformedURLException e) {
//            return null;
//        } catch  (ProtocolException e){
//
//        }catch(IOException e){
//
//        }
//        finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//
//                }
//            }
//        }
//        getTrailers(RESULT);
//        return RESULT;
//    }
//
//    private static void getTrailers(String result) {
//        try {
//            JSONObject moviesObject = new JSONObject(result);
//            Integer movieId = moviesObject.getInt("id");
//            Movie movie=null;
//            for(Movie m: getMovies()){
//                if(m.getId()==movieId)
//                    movie=m;
//            }
//            Trailer trailer = new Trailer();
//            JSONArray moviesArray = moviesObject.getJSONArray("results");
//            for (int i = 0; i < moviesArray.length(); i++) {
//                JSONObject newMovie = moviesArray.getJSONObject(i);
//                trailer.setId(newMovie.getString("id"));
//                trailer.setIso_639_1(newMovie.getString("iso_639_1"));
//                trailer.setKey(newMovie.getString("key"));
//                trailer.setName(newMovie.getString("name"));
//                trailer.setSite(newMovie.getString("site"));
//                trailer.setSize(newMovie.getString("size"));
//                if(movie.getTrailers()==null)
//                    movie.setTrailers(new ArrayList<Trailer>());
//                movie.getTrailers().add(trailer);
//
//            }
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
