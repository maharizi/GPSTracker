package com.example.gpsenglish.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gpsenglish.Class.Person;
import com.example.gpsenglish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextname, editTextemail, editTextphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextname = findViewById(R.id.editTextname);
        editTextemail = findViewById(R.id.editTextemail);
        editTextphone = findViewById(R.id.editTextphone);

        mAuth = FirebaseAuth.getInstance();
        GetFromDB();
    }

    public void GetFromDB() {

        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid);

        // Read and write from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Person p = dataSnapshot.getValue(Person.class);
                editTextname.setText(p.getName());
                editTextemail.setText(p.getEmail());
                editTextphone.setText(p.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Profile.this, "error.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view) {

        // Connection to the database
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("person").child(uid);

        String name = editTextname.getText().toString();
        String email = editTextemail.getText().toString();
        String phone = editTextphone.getText().toString();

        // write to database
        try {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            myRef.child("name").setValue(name);
            myRef.child("email").setValue(email);
            myRef.child("phone").setValue(phone);
            Toast.makeText(Profile.this, "change details success.",
                    Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {

            Toast.makeText(Profile.this, "change details failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}