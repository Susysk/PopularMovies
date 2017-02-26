package com.example.android.popularmovies;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.data.APISingleton;
import com.example.android.popularmovies.data.DBHelper;
import com.example.android.popularmovies.data.MovieAsyncTask;
import com.example.android.popularmovies.data.MovieFavoriteAsyncTask;
import com.example.android.popularmovies.data.MovieTrailerAsyncTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  public static MovieAdapter movieAdapter;
    static public String lastCriteria;
    static GridView gridview;
    public static APISingleton singleton;
    private final String TAG_TASK_FRAGMENT = "task_fragment";
    public static BroadcastReceiver receiver;
    protected TaskFragment taskFragment;
    MainActivityFragment fragment;
    public boolean pref=false;
    public static MovieTrailerAsyncTask movieTrailerAsyncTask;
    public static   boolean isTablet;
    public static BroadcastReceiver getReceiver() {
        return receiver;
    }

    public static void setReceiver(BroadcastReceiver receiver) {
        MainActivity.receiver = receiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singleton = APISingleton.getInstance(this);
        singleton.setActivity(this);

        DBHelper db = new DBHelper(this);
        singleton.setDb(db);
        lastCriteria = null;
        movieTrailerAsyncTask = new MovieTrailerAsyncTask();
        movieTrailerAsyncTask.setActivity(this);
         isTablet=getResources().getBoolean(R.bool.isTablet);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.container_movies)!=null){
            if (savedInstanceState == null) {
                fragment = new MainActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment, fragment, "")
                        .commit();
                DetailsActivityFragment f2 = new DetailsActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_movies, f2, "")
                        .commit();
            }
            else
                fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        }
        setupTaskFragment();
        if(isConnectedViaWifi()==false){
            startActivity(new Intent(getApplication(), ErrorActivity.class).putExtra("from","main").setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.i("error", "on broadcast receive");
                    final String action = intent.getAction();
                    if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                        Log.i("error", "on broadcast receive Wifi");
                        if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {

                        } else {
                            Log.i("error", "on broadcast connected");
                            startActivity(new Intent(getApplication(), ErrorActivity.class).putExtra("from","main").setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));

                            finish();
                        }
                    }
                }
            };
            registerReceiver(receiver, intentFilter);
    }
    private boolean isConnectedViaWifi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getActiveNetworkInfo();
        return mWifi !=null && mWifi.isConnectedOrConnecting();
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    protected void setupTaskFragment() {
        FragmentManager fm = getSupportFragmentManager();
        taskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            fm.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
            fm.executePendingTransactions();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.action_sort_rating){
            pref=false;
            MainActivity.gridview.setAdapter(null);
            MainActivity.gridview.setAdapter(MainActivity.movieAdapter);
            MainActivity.movieAdapter.setFav(0);
            new MovieAsyncTask().execute("top_rated", null);
        }
        if(id== R.id.action_sort_popularity){
            pref=false;
            MainActivity.gridview.setAdapter(null);
            MainActivity.gridview.setAdapter(MainActivity.movieAdapter);
            MainActivity.movieAdapter.setFav(0);
            new MovieAsyncTask().execute("popular", null);
        }
        if(id==R.id.action_sort_favorites){
            pref=true;
            MainActivity.gridview.setAdapter(null);
            MainActivity.gridview.setAdapter(MainActivity.movieAdapter);
            MainActivity.movieAdapter.setFav(1);
            new MovieFavoriteAsyncTask(getContentResolver(),getApplicationContext()).execute();
            if(singleton.getFavImages().isEmpty())
                Toast.makeText(getApplicationContext(),"There is no favorite",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
    public static class TaskFragment extends Fragment {

        private ArrayList<AsyncTask> asyncTasks = new ArrayList<AsyncTask>();

        public TaskFragment() {
            super();
            setRetainInstance(true);
        }

        public void addTask(AsyncTask task) {
            asyncTasks.add(task);
        }

        public void removeTask(AsyncTask task) {
            asyncTasks.remove(task);
        }

        public Application getApplication() {
            Activity activity = getActivity();
            if (activity != null)
                return activity.getApplication();
            else
                return null;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            for (AsyncTask task : asyncTasks) {
                task.execute(activity);
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            for (AsyncTask task : asyncTasks) {
                task.cancel(true);
            }
            }
        }
}
