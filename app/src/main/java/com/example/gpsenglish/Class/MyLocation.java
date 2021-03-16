package com.example.gpsenglish.Class;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyLocation extends Application {
    private static MyLocation singleton;
    private List<Location> myLocations;

    // getters
    public List<Location> getMyLocations() {
        return myLocations;
    }

    public MyLocation getInstance(){
        return singleton;
    }

    // setter
    public void setMyLocation(List<Location> myLocation) {
        this.myLocations = myLocation;
    }

    public void onCreate(){
        super.onCreate();

        singleton = this;
        myLocations = new ArrayList<>();

    }

}
