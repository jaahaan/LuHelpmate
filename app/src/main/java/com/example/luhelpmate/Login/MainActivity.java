package com.example.luhelpmate.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.luhelpmate.HomeActivity;
import com.example.luhelpmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int GOOGLE_SIGN_IN_CODE = 105;
    SignInButton SignIn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog pd;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("885754194582-a9eho8kf4i7a0pj43gq0mlpldeiriico.apps.googleusercontent.com")
                .requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, gso);

        SignIn = findViewById(R.id.button);

        SignIn.setOnClickListener(v -> {
            Intent intent = signInClient.getSignInIntent();
            startActivityForResult(intent, GOOGLE_SIGN_IN_CODE);
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            pd.setMessage("Loading...");
            pd.show();
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = signInAccountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                mAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //check(authResult.getUser().getUid());
                        //For institutional email verification
                        if (authResult.getUser().getEmail().matches("(cse_)[\\d]{10}(@lus.ac.bd)") || authResult.getUser().getEmail().matches("[a-b A-Z]+(_cse)?(@lus.ac.bd)") || authResult.getUser().getEmail().matches("njlisa25@gmail.com")) {
                            check(authResult.getUser().getUid());
                        } else {
                           FirebaseAuth.getInstance().signOut();
                            GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Select Your Institutional Email.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void check(String uid) {
        DocumentReference df = firestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("admin") == null) {
                    try {
                        FirebaseUser user = mAuth.getCurrentUser();
                        DocumentReference df = firestore.collection("Users").document(user.getUid());
                        Map<String, String> userInfo = new HashMap<>();
                        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        userInfo.put("email", signInAccount.getEmail());
                        userInfo.put("name", signInAccount.getDisplayName());
                        userInfo.put("designation", "");
                        userInfo.put("initial", "");
                        userInfo.put("department", "");
                        userInfo.put("admin", "2");
                        userInfo.put("uid", user.getUid());
                        df.set(userInfo);
                        startActivity(new Intent(getApplicationContext(), ProvideInfo.class));
                        finish();
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Account Created Successfully!!!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (documentSnapshot.getString("initial").equals("")) {
                    try {
                        FirebaseUser user = mAuth.getCurrentUser();
                        DocumentReference df = firestore.collection("Users").document(user.getUid());
                        Map<String, String> userInfo = new HashMap<>();
                        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                        userInfo.put("email", signInAccount.getEmail());
                        userInfo.put("name", signInAccount.getDisplayName());
                        userInfo.put("designation", "");
                        userInfo.put("initial", "");
                        userInfo.put("department", "");
                        userInfo.put("admin", "2");
                        userInfo.put("uid", user.getUid());
                        df.set(userInfo);
                        pd.dismiss();
                        startActivity(new Intent(getApplicationContext(), ProvideInfo.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}