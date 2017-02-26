package com.example.android.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
public class ErrorActivity extends AppCompatActivity {
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        System.out.println(i.getStringExtra("from"));
        String from = i.getStringExtra("from");
        setContentView(R.layout.activity_error);
        Log.i("on create","on create error");
        if(isConnectedViaWifi()==true) {
            Log.i("on create","connected");
            finish();
        }
        Picasso.with(getApplicationContext()).load(R.drawable.trex)
               .into((ImageView) findViewById(R.id.error));
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
                        Log.i("error","on broadcast connected");
                        startActivity(new Intent(getApplication(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        finish();
                    } else {
                        // wifi connection was lost
                    }
                }
            }
        };
        registerReceiver(receiver,intentFilter);

    }
    @Override
    public void onDestroy(){
            super.onDestroy();
        Log.i("on destoy","on destroy ");
        this.unregisterReceiver(receiver);
    }
    private boolean isConnectedViaWifi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }


}
