package com.example.durai23.runningtrackingapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Durai23 on 15/12/2017.
 */

public class LocationListenerHandler extends ContextWrapper implements LocationListener{
    static final String TAG = "RunApp";

    public LocationListenerHandler(Context base) {
        super(base);
    }

    @Override
    public void onLocationChanged(Location location) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putDouble("Latitude",location.getLatitude());
        bundle.putDouble("Longitude",location.getLongitude());
        intent.putExtras(bundle);
        intent.setAction("CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged: " + provider + " " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "GPS enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "GPS disabled");
    }


}
