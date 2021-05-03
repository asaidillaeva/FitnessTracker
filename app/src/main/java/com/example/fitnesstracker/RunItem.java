package com.example.fitnesstracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "runItem")
public class RunItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rid")
    private long id;
    private String nameOfRun;
    private double distance;
    private double speed;
    private long millis;
    private String imageMap;
    private String date;
    private String time;


    public RunItem(String nameOfRun, double distance, double speed, long millis, String imageMap, String date, String time) {
        this.nameOfRun = nameOfRun;
        this.distance = distance;
        this.speed = speed;
        this.millis = millis;
        this.imageMap = imageMap;
        this.date = date;
        this.time = time;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNameOfRun(String nameOfRun) {
        this.nameOfRun = nameOfRun;
    }

    public long getId() {
        return id;
    }

    public String getNameOfRun() {
        return nameOfRun;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }


    public String getImageMap() {
        return imageMap;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public void setImageMap(String imageMap) {
        this.imageMap = imageMap;
    }
}
