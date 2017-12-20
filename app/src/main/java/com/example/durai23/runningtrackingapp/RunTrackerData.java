package com.example.durai23.runningtrackingapp;

/**
 * Created by Durai23 on 16/12/2017.
 */

public class RunTrackerData {
    private int _id;
    private String date;
    private String distance;
    private String totalTime;

    public RunTrackerData(){
    }

    public RunTrackerData(String date, String distance, String time){
        this.date = date;
        this.distance = distance;
        this.totalTime = time;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public int getID() {
        return _id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {return date;}

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

}
