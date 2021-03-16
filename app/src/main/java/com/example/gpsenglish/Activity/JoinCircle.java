package com.example.gpsenglish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gpsenglish.Class.Person;
import com.example.gpsenglish.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinCircle extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef, myCurrentRef, circleRef;
    String uid, joinUserId, currentUserId;
    EditText editTextCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        editTextCode = findViewById(R.id.editTextCode);

        // Connection to the database
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("person");
        myCurrentRef = database.getReference("person").child(uid).child(user.getUid());
        currentUserId = user.getUid();
    }

    public void SubmitButtonClick(View view) {

        Query query = myRef.orderByChild("code").equalTo(String.valueOf(editTextCode.getText()));

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    // if the code is exist

                    Person person = null;
                    for(DataSnapshot childDss : dataSnapshot.getChildren())
                    {
                        person = childDss.getValue(Person.class);
                        joinUserId = person.getUserId();
                        circleRef = database.getReference().child("person").child(currentUserId).child("circleUid");

                        circleRef.child("uid").setValue(joinUserId).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                    Toast.makeText(getApplicationContext(), "Child location added successfully", Toast.LENGTH_SHORT).show();

                                    database.getReference().child("person").child(currentUserId).child("isSharing").setValue("true");

                                    Intent i = new Intent(JoinCircle.this, Maps.class);
                                    startActivity(i);

                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "This code Not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}