package com.example.gpsenglish.Activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsenglish.Class.MyLocation;
import com.example.gpsenglish.Class.Person;
import com.example.gpsenglish.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 99;

    private GoogleMap mMap;

    private FirebaseAuth mAuth;
    TextView textViewName, tv_Address2, tv_updates2;
    Switch sw_locationsupdates2;
    Button buttonOptions;

    // button's JoinCircle
    Button buttonSubmit;
    TextView textViewChildName, textViewChildAddress;

    // current location
    Location currentLocation;

    // list of saved location
    static List<Location> savedLocations;

    //Location request is a config file for all settings related to FusedLocationProviderClient.
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    // Google's API for location services. The majority of the app functions using this class.
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textViewName = findViewById(R.id.textViewName);
        tv_Address2 = findViewById(R.id.tv_Address2);
        tv_updates2 = findViewById(R.id.tv_updates2);
        sw_locationsupdates2 = findViewById(R.id.sw_locationsupdates2);
        buttonOptions = findViewById(R.id.buttonOptions);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewChildName = findViewById(R.id.textViewChildName);
        textViewChildAddress = findViewById(R.id.textViewChildAddress);

        // set all properties of LocationRequest
        locationRequest = new LocationRequest();

        // how often does the default location check occur?
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);

        // how often does the location check occur when set to the most frequent update?
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // event that is triggered whenever the update interval is met.
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // save the location
                Location location = locationResult.getLastLocation();
                updateUIValues(location);

            }
        };

        // Go to Options activity
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Maps.this, Options.class); //mean to over
                startActivity(intent); //over
            }
        });

        sw_locationsupdates2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sw_locationsupdates2.isChecked()) {
                    // turn on location tracking
                    startLocationUpdates();
                } else {
                    // turn off tracking
                    stopLocationUpdates();
                }
            }
        });

        MyLocation myLocation = (MyLocation)getApplicationContext();
        savedLocations = myLocation.getMyLocations();

        // read the name from DB, and write the information
        mAuth = FirebaseAuth.getInstance();
        GetFromDB();

        updateGPS();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void stopLocationUpdates() {

        tv_updates2.setText("לא מבוצע מעקב מיקום");
        tv_Address2.setText("אין מיקום");
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {

        tv_updates2.setText("מבוצע מעקב מיקום");
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack,null);
        updateGPS();

    }



    private void updateGPS(){
        // get permissions from the user to track GPS
        // get the current location from the fused client
        // update the UI - i.e set all properties in their associating text view items.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Maps.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user provided the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permission, put the value of location. xxx into the UI components.
                    updateUIValues(location);
                    currentLocation = location;

                    // start the map with the current location
                    if(savedLocations.size() == 0)
                        savedLocations.add(currentLocation);

                }
            });
        }
        else {
            // permissions not granted yet.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);

            }
        }
    }

    private void updateUIValues(Location location) {
        // update all of the text view objects with a new location.
        saveFirebase(location);


        // write the address
        Geocoder geocoder = new Geocoder(Maps.this);
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_Address2.setText(addresses.get(0).getAddressLine(0));
        }
        catch (Exception e) {
            tv_Address2.setText("Unable to get street address");
        }

        MyLocation myLocation = (MyLocation)getApplicationContext();
        savedLocations = myLocation.getMyLocations();

    }

    private void saveFirebase(Location location) {

        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid);

        // write to database
        try {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            myRef.child("lat").setValue(String.valueOf(location.getLatitude()));
            myRef.child("lng").setValue(String.valueOf(location.getLongitude()));
            Toast.makeText(Maps.this, "Location Update",
                    Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {

            Toast.makeText(Maps.this, "Location Not Update",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services isd to install
     * it inside the SupportMapFragment. This method will only be triggered once the us not installed on the device, the user will be prompteer has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-35, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng lastLocationPlaced = sydney;

        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid);

        // Current person
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person p = snapshot.getValue(Person.class);
                if(p.getIsSharing().length() == 4)
                    joinLocation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Maps.this, "error.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // add my locations to the map
        for (Location location: savedLocations) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Me");
            mMap.addMarker(markerOptions);
            lastLocationPlaced = latLng;

            // write the address
            Geocoder geocoder = new Geocoder(Maps.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                tv_Address2.setText(addresses.get(0).getAddressLine(0));
            } catch (Exception e) {
                tv_Address2.setText("Unable to get street address");
            }
        }

        // zoom into the last dropped pin
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, 9.0f));
        
    }

    private void joinLocation() {

        final ArrayList<String> list = new ArrayList<>();

        int i = 0;
        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid).child("circleUid");

        // the user we can see
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                Person p = dataSnapshot.getValue(Person.class);

                // somebody else location
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list.add(snapshot.getValue().toString());
                    Log.d("result", list.get(0));

                    DatabaseReference secRef = database.getReference("person").child(list.get(i));
                    // Read and write from the database
                    secRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Person p = dataSnapshot.getValue(Person.class);
                            textViewChildName.setText(p.getName());

                            if (p.getLat().length() != 4 && p.getLng().length() != 4) {
                                LatLng userLocation = new LatLng(Double.valueOf(p.getLat()), Double.valueOf(p.getLng()));
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(userLocation);
                                markerOptions.title(p.getName());
                                mMap.addMarker(markerOptions);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 9.0f));

                                // write the address
                                Geocoder geocoder = new Geocoder(Maps.this);
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(Double.valueOf(p.getLat()), Double.valueOf(p.getLng()), 1);
                                    textViewChildAddress.setText(addresses.get(0).getAddressLine(0));
                                } catch (Exception e) {
                                    textViewChildAddress.setText("Unable to get street address");
                                }

                            } else
                                Toast.makeText(Maps.this, "The user exist but his location is null", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Toast.makeText(Maps.this, "error.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Maps.this, "error.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GetFromDB() {
        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Person p = dataSnapshot.getValue(Person.class);
                textViewName.setText("שלום " + p.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

}