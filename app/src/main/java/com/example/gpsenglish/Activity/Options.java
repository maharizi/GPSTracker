package com.example.gpsenglish.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.gpsenglish.Class.Person;
import com.example.gpsenglish.Fragment.Settings;
import com.example.gpsenglish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Options extends AppCompatActivity {

    private static boolean flag = false;
    private FirebaseAuth mAuth;
    private FragmentManager fragmentManager;

    // Settings
    TextView textViewname,textViewemail, textViewCode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // start fragment settings
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();//mean to start the fragment setting
        fragmentTransaction.add(R.id.frameoptions, new Settings()).commit();//start the fragment setting

    }

    @Override
    protected void onStart() {
        super.onStart();

        // read the name from DB, and write the information
        textViewname = findViewById(R.id.textViewname);
        textViewCode2 = findViewById(R.id.textViewCode2);
        textViewemail = findViewById(R.id.textViewemail);
        mAuth = FirebaseAuth.getInstance();
        GetFromDB();

    }

    // over to Profile
    public void GoToProfile() {

        Intent intent = new Intent(Options.this, Profile.class); //mean to over
        startActivity(intent); //over

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
                textViewname.setText(p.getName());
                textViewemail.setText(p.getEmail());
                textViewCode2.setText(" " + p.getCode());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    // over to Call
    public void GoToCall() {

        Intent intent = new Intent(Options.this, Call.class); //mean to over
        startActivity(intent); //over Call

    }

    // Logout
    public void Logout(View view) {

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("MainKEY", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("keyEmail").commit();
        editor.remove("keyPass").commit();

        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void GoToJoinCircle() {

        Intent intent = new Intent(Options.this, JoinCircle.class); //mean to over
        startActivity(intent); //over Call

    }
}