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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

    }

    public void RegFunc(View view) {

        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        String code = String.valueOf(n);

        EditText editTextName = findViewById(R.id.editTextName);
        String name = editTextName.getText().toString();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        String email = editTextEmail.getText().toString();

        EditText editTextPhone = findViewById(R.id.editTextPhone);
        String phone = editTextPhone.getText().toString();

        EditText editTextPassword = findViewById(R.id.editTextPassword);
        String password = editTextPassword.getText().toString();

        EditText editTextPassword2 = findViewById(R.id.editTextPassword2);
        String password2 = editTextPassword2.getText().toString();

        if (name.length() > 1) {
            if (phone.length() == 10) {
                if (password.length() > 5) {
                    if (password.equals(password2) == true) {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(Register.this, "register ok.",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            // Write a message to the database
                                            String uid = user.getUid();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("person").child(uid);
                                            Person p = new Person("null", name, phone, email, code, "null", "null", "false", user.getUid());
                                            myRef.setValue(p);
                                            Intent intent = new Intent(Register.this, Login.class); //mean to over
                                            startActivity(intent); //over


                                        } else {
                                            // If sign in fails, display a message to the user.

                                            Toast.makeText(Register.this, "Email - wrong",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });

                    } else
                        Toast.makeText(Register.this, "Password confirmation - different from password !",
                                Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(Register.this, "Password  - minimum 6 digits !",
                            Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(Register.this, "Phone - 10 digits !",
                        Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(Register.this, "Name - minimum 2 letters !",
                    Toast.LENGTH_SHORT).show();
    }

}