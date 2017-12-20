package com.example.durai23.runningtrackingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewDataActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        listView = (ListView)findViewById(R.id.listView);

        listAllData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = parent.getItemAtPosition(position).toString();
                findSingleData(date);
            }
        });

    }

    public void listAllData(){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        RunTrackerData data = dbHandler.listAllData();

        if(data != null){
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dbHandler.getDataList());
            listView.setAdapter(adapter);
        }
        else {
            Toast.makeText(this, "No data",Toast.LENGTH_SHORT).show();
        }
    }

    public void findSingleData(String date){
        DBHandler dbHandler = new DBHandler(this, null, null, 1);
        RunTrackerData data = dbHandler.findSingleData(date);

        //set data to be viewed
        Bundle bundle = new Bundle();
        bundle.putInt("data_ID",data.getID());
        bundle.putString("date",data.getDate());
        bundle.putString("distance",data.getDistance());
        bundle.putString("time",data.getTotalTime());
        Intent intent = new Intent(ViewDataActivity.this, ViewSingleDataActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1000);//starts view activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            listAllData();
        }
    }
}
