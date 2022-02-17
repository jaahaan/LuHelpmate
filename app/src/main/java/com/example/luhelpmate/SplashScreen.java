package com.example.luhelpmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luhelpmate.Admin.HomeActivityAdmin;
import com.example.luhelpmate.User.HomeActivityUser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private SpinKitView spinKitView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        spinKitView = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
                startApp();
            }
        });
        thread.start();
    }

    /**private void check(String uid) {
        try {
            DocumentReference df = firestore.collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("admin") != null) {
                        if (documentSnapshot.getString("admin").equals("1")) {
                            startActivity(new Intent(getApplicationContext(), HomeActivityAdmin.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();

                        } else if (documentSnapshot.getString("admin").equals("2")) {
                            startActivity(new Intent(getApplicationContext(), HomeActivityUser.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    } else{
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

    }*/

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
                startActivity(new Intent(getApplicationContext(), HomeActivityAdmin.class));
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}