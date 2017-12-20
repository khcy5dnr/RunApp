package com.example.durai23.runningtrackingapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service {
    static final String TAG = "RunApp";

    public LocationService() {}

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service started");
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListenerHandler locationLister = new LocationListenerHandler(this);

        try{
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1,locationLister);
            }

        }
        catch (SecurityException e){
            Log.i(TAG,e.toString());
        }
        return START_STICKY;
    }

}
