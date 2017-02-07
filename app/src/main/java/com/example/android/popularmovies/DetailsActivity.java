package com.example.android.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.host.APISingleton;
import com.example.android.popularmovies.host.MovieAsyncTask;
import com.example.android.popularmovies.movies.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    public static Movie movie;
    public static Intent intent;
    public static TextView movie_release;
    public static TextView movie_title;
    public static TextView movie_year;
    public static TextView movie_overview;
    public static TextView movie_rate;
    public static RatingBar rating;
    public static ImageView movie_poster;
    public static APISingleton singleton;
    public static BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        singleton = APISingleton.getInstance(this);
        singleton.setActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("error","on broadcast receive");
                final String action = intent.getAction();
                if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                    Log.i("error","on broadcast receive Wifi");
                    if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){

                    } else {
                        Toast.makeText(context,"Wifi connection was lost",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        intent= getIntent();
        registerReceiver(receiver,intentFilter);
        movie = new Movie();
        int movie_position = intent.getIntExtra("position",0);
        movie = singleton.movies.get(movie_position);
        movie_title = (TextView)findViewById(R.id.movie_title);
        movie_rate = (TextView)findViewById(R.id.movie_rating);
        movie_poster = (ImageView)findViewById(R.id.movie_poster);
        movie_overview = (TextView)findViewById(R.id.movie_overview);
        movie_year = (TextView)findViewById(R.id.movie_year);
        movie_release = (TextView)findViewById(R.id.movie_release);
        try {
            movie_title.setText(movie.getTitle());
            movie_year.setText(movie.getRelease().substring(0, 4));
            movie_title.setVisibility(View.VISIBLE);
            movie_rate.setText(movie.getVote_average() + "/10");
            movie_overview.setText(movie.getOverview());
            rating = (RatingBar) findViewById(R.id.rating);
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
            movie_release.setText("Release on: " + movie.getRelease());
            String movie_poster_url;
            if (movie.getPosterPath().equals(APISingleton.getImageNotFound())) {
                movie_poster_url = APISingleton.getImageUrl();
            } else {
                movie_poster_url = APISingleton.getImageUrl() + APISingleton.getImageSize() + "/" + movie.getPosterPath();
            }
            Picasso.with(getApplicationContext()).load(movie_poster_url).into(movie_poster);
            movie_poster.setVisibility(View.VISIBLE);
        }
        catch(Exception e){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.action_sort_rating){
            new MovieAsyncTask().execute("top_rated", null);
        }
        if(id== R.id.action_sort_popularity){
            new MovieAsyncTask().execute("popular", null);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
