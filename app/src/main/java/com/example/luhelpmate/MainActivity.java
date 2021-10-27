package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button signUp, signIn;
    TextView alreadyIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = findViewById(R.id.button1);
        signIn = findViewById(R.id.button2);
        alreadyIn = findViewById(R.id.alreadyIn);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
        alreadyIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }

        if (v.getId() == R.id.button2) {
            Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signInIntent);
        }
        if (v.getId() == R.id.alreadyIn) {
            Intent alreadyInIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(alreadyInIntent);
            finish();
        }
    }
}