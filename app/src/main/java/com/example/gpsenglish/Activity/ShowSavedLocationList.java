package com.example.gpsenglish.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gpsenglish.Class.MyLocation;
import com.example.gpsenglish.R;

import java.util.List;

public class ShowSavedLocationList extends AppCompatActivity {

    ListView lv_savedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location_list);

        lv_savedLocations = findViewById(R.id.lv_wayPoints);
        
        MyLocation myLocation = (MyLocation)getApplicationContext();
        List<Location> savedLocation = myLocation.getMyLocations();

        lv_savedLocations.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, savedLocation));
    }
}