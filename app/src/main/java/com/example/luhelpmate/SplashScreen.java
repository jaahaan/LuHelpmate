package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.luhelpmate.Login.MainActivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    private SpinKitView spinKitView;

    private FirebaseAuth mAuth;
    //private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        spinKitView = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        //firestore = FirebaseFirestore.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        thread.start();
    }


    public void doWork() {
        for (int progress = 50; progress <= 100; progress += 50) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startApp() {
        try {
            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (signInAccount != null || mAuth.getCurrentUser() != null) {
                //check(mAuth.getCurrentUser().getUid());
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
    @Override
    public void onStop() {
        super.onStop();
    }*/

}