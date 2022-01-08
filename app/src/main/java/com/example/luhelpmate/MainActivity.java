package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.luhelpmate.Admin.AdminSignInActivity;
import com.example.luhelpmate.User.studentSignIn;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button sSignIn, tSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sSignIn = findViewById(R.id.button1);
        tSignIn = findViewById(R.id.button2);

        sSignIn.setOnClickListener(this);
        tSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            Intent signUpIntent = new Intent(MainActivity.this, studentSignIn.class);
            startActivity(signUpIntent);
        }

        if (v.getId() == R.id.button2) {
            Intent intent = new Intent(MainActivity.this, AdminSignInActivity.class);
            startActivity(intent);
        }

    }
}