package com.example.gpsenglish.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gpsenglish.R;

public class Call extends AppCompatActivity {

    EditText editTextPhoneCall;
    ImageButton imageButtonCall;

    //permission to call
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // call to person
        editTextPhoneCall = findViewById(R.id.editTextPhoneCall);
        imageButtonCall = findViewById(R.id.imageButtonCall);
        editTextPhoneCall.setText("100");

        imageButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editTextPhoneCall.getText().toString();
                if (phone.isEmpty())
                    Toast.makeText(getApplicationContext(), "Please Enter Number !"
                            , Toast.LENGTH_SHORT).show();
                else {
                    if (ContextCompat.checkSelfPermission(Call.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        // user provided the permission
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                    } else {
                        // permissions not granted yet.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    }
                }


            }
        });
    }
}