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

import com.example.android.popularmovies.data.APISingleton;
import com.example.android.popularmovies.data.MovieAsyncTask;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    static public String lastCriteria;
    public static APISingleton singleton;
    private final String TAG_TASK_FRAGMENT = "task_fragment";
    public static BroadcastReceiver receiver;
    protected DetailsActivity.TaskFragment taskFragment;
    public static int idMovie;
    public static int movie_Position;
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
        lastCriteria = null;
        Intent intent = getIntent();
       movie_Position = intent.getIntExtra("position",0);

            idMovie = intent.getIntExtra("id",0);
        setContentView(R.layout.activity_details);
        setupTaskFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new DetailsActivityFragment())
                    .commit();
        }

        if(isConnectedViaWifi()==false){
            startActivity(new Intent(getApplication(), ErrorActivity.class).putExtra("from","details")
                    .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
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
                        startActivity(new Intent(getApplication(), ErrorActivity.class).putExtra("from","details").setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));

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
        if(receiver!=null)
            unregisterReceiver(receiver);
    }
    protected void setupTaskFragment() {
        FragmentManager fm = getSupportFragmentManager();
        taskFragment = (DetailsActivity.TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment == null) {
            taskFragment = new DetailsActivity.TaskFragment();
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
            MainActivity.gridview.setAdapter(null);
            MainActivity.gridview.setAdapter(MainActivity.movieAdapter);
            new MovieAsyncTask().execute("top_rated", null);
        }
        if(id== R.id.action_sort_popularity){
            MainActivity.gridview.setAdapter(null);
            MainActivity.gridview.setAdapter(MainActivity.movieAdapter);
            new MovieAsyncTask().execute("popular", null);
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