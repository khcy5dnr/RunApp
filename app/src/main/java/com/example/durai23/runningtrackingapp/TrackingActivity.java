package com.example.durai23.runningtrackingapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener{

    static final String TAG = "RunApp";
    private Button start,stop;
    private TextView distanceText, totalTimeText;
    private Handler handler;
    private Location startPosition, endPosition;
    private Location tempLocation;
    private double latitude,longitude, tempDistance;
    private float distance;
    private String date, time;
    private SimpleDateFormat dateFormat, timeFormat;
    private Chronometer chronometer;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking2);

        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        distanceText = (TextView)findViewById(R.id.distance);
        totalTimeText = (TextView)findViewById(R.id.totalTime);
        chronometer = (Chronometer)findViewById(R.id.chronometer2) ;

        handler = new Handler();

        stop.setClickable(false);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        tempLocation = new Location("tempLocation");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                latitude = bundle.getDouble("Latitude");
                longitude = bundle.getDouble("Longitude");


                tempLocation.setLatitude(latitude);
                tempLocation.setLongitude(longitude);

            }
        };

        this.registerReceiver(broadcastReceiver,new IntentFilter("CUSTOM_INTENT"));

        dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start:
                //reset time text
                totalTimeText.setText("00:00");

                //start stopwatch
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                //start thread to get location
                handler.postDelayed(runnable, 0);

                //get start location
                startPosition = new Location("startPosition");
                startPosition.setLatitude(latitude);
                startPosition.setLongitude(longitude);

                //get date
                date = dateFormat.format(Calendar.getInstance().getTime());

                stop.setClickable(true);
                break;

            case R.id.stop:
                //stop stopwatch
                chronometer.stop();

                start.setText("START");

                //stop thread from getting location
                handler.removeCallbacks(runnable);

                //get finish position
                endPosition = new Location("endPosition");
                endPosition.setLatitude(latitude);
                endPosition.setLongitude(longitude);

                //calculate distance and display
                distance = startPosition.distanceTo(endPosition);
                distanceText.setText(String.format("%.2f",+distance));

                //display total time
                totalTimeText.setText(chronometer.getText().toString());

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                // create alert box when delete is clicked
                builder.setTitle("Save Data?")
                        .setMessage("Are you sure you want to save this session's data?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                                //add to database
                                DBHandler dbHandler = new DBHandler(TrackingActivity.this, null, null, 1);
                                RunTrackerData data = new RunTrackerData(date,Double.toString(Math.round(distance)),
                                        totalTimeText.getText().toString());
                                dbHandler.addData(data);

                                Toast.makeText(TrackingActivity.this, "Data saved.", Toast.LENGTH_SHORT).show();
                                Log.i(TAG,"Data saved.");
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Log.i(TAG,"Data not saved.");
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            default:
                Log.i(TAG,"Error");
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            if(tempLocation.getLatitude() != 0.0 || tempLocation.getLongitude() != 0.0){
                tempDistance = startPosition.distanceTo(tempLocation);
                distanceText.setText(String.format("%.2f",+tempDistance));
            }
            handler.postDelayed(this, 0);
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiver,new IntentFilter("CUSTOM_INTENT"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
