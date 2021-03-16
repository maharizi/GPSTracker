package com.example.gpsenglish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsenglish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText TextEmail, TextPassword;
    TextView textViewRegister, textViewForgotPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MainKEY", 0);//save the details in mode private
        TextEmail = findViewById(R.id.TextEmail);
        TextPassword = findViewById(R.id.TextPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        if(sharedPreferences.getString("keyEmail", null) != null)//if exist details
        {
            //Enter a second time, transfer directly to the maps
            /*Intent intent = new Intent(this,Calculator.class);
            intent.putExtra("keyEmail", R.id.passwordText);
            startActivity(intent);*/

            //Enter a second time, completes details automatically
            TextEmail.setText(sharedPreferences.getString("keyEmail", null));
            TextPassword.setText(sharedPreferences.getString("keyPass", null));
        }

        // register
        textViewRegister.setOnClickListener(new View.OnClickListener() { //inner class - click the button, and the fragment is start
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class); //mean to over
                startActivity(intent); //over

            }
        });

        // reset pass
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start Dialog
                EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Reset Password");
                passResetDialog.setMessage("Enter Your Email:");
                passResetDialog.setView(resetMail);

                // click on send
                passResetDialog.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link

                        String mail = resetMail.getText().toString();

                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Send To Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error !", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                // click on go back
                passResetDialog.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passResetDialog.create().show();

            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    public void loginFunc(View view) {

        EditText emailText = findViewById(R.id.TextEmail);
        String email = emailText.getText().toString();

        EditText passText = findViewById(R.id.TextPassword);
        String password = passText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "login ok.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            sharedPreferences = getSharedPreferences("MainKEY", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("keyEmail", emailText.getText().toString());//save the last details
                            editor.putString("keyPass", passText.getText().toString());//save the last details
                            editor.apply();

                            GoMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }

                });

    }

    private void GoMain() {
        Intent intent = new Intent(Login.this, Maps .class); //mean to over
        startActivity(intent); //over
    }

}