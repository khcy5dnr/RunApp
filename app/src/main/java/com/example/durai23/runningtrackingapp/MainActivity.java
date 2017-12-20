package com.example.durai23.runningtrackingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final String TAG = "RunApp";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(getBaseContext(),LocationService.class);

        int checkGPS_status = 0;
        try {
            checkGPS_status = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            if(checkGPS_status==0){
                Intent turnOnGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(turnOnGPS);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


        Button startTracking = (Button)findViewById(R.id.startTracking_btn);
        Button viewData = (Button)findViewById(R.id.dataView_btn);

        //handled onClick method
        startTracking.setOnClickListener(this);
        viewData.setOnClickListener(this);

        //check for permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Request the permission.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                startService(intent);
                break;
            default:
                Log.i(TAG,"Error on permission.");
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){//checks button IDs
            case R.id.startTracking_btn:
                Intent intentStartTracking = new Intent(this,TrackingActivity.class);
                startActivity(intentStartTracking);//start Tracking activity
                Log.i(TAG,"Tracking Activity started.");
                break;
            case R.id.dataView_btn:
                Intent intentViewData = new Intent(this,ViewDataActivity.class);
                startActivity(intentViewData);//start View tracking data activity
                Log.i(TAG,"View data Activity started.");
                break;
            default:
                Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
