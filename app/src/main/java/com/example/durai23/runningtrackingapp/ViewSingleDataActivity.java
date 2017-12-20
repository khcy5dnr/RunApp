package com.example.durai23.runningtrackingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSingleDataActivity extends AppCompatActivity {

    private TextView date, distance, timeTaken;
    private Bundle bundle;
    static final String TAG = "RunApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_data);

        bundle = getIntent().getExtras();

        date = (TextView)findViewById(R.id.date);
        distance = (TextView)findViewById(R.id.distance);
        timeTaken = (TextView)findViewById(R.id.timeTaken);

        date.setText(bundle.getString("date"));
        distance.setText(bundle.getString("distance"));
        timeTaken.setText(bundle.getString("time"));
    }

    public void deleteData(View view){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        // create alert box when delete is clicked
        builder.setTitle("Delete Recipe?")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        DBHandler dbHandler = new DBHandler(ViewSingleDataActivity.this, null, null, 1);
                        boolean flag = dbHandler.deleteRecipe(bundle.getInt("data_ID"));

                        if(flag){
                            Toast.makeText(ViewSingleDataActivity.this, "Data deleted", Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"Data deleted.");
                            finish();
                        }
                        else{
                            Toast.makeText(ViewSingleDataActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                            Log.i(TAG,"Data not found.");
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(ViewSingleDataActivity.this, "Data not deleted", Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"Recipe not deleted.");
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
