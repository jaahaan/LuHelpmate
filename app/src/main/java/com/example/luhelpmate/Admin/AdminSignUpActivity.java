package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RC_SIGN_IN = 12;
    private EditText signUpEmail, signUpPass;
    private Button signUpButton, signUpGmail;
    private TextView login;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpButton);
        signUpGmail = findViewById(R.id.signUpGmailF);
        progressBar = findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();

        login.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        signUpGmail.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signUpButton) {
            userRegister();
        }
        if(v.getId()==R.id.signUpGmailF) {
            signIn();
        }
        else if(v.getId()==R.id.login) {
            Intent intent = new Intent(AdminSignUpActivity.this, AdminSignInActivity.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
/**
    private void handleSignUpResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(this);
            if (user != null) {
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();
                String email = user.getEmail();
                String id = user.getId();
                Uri photoUrl = user.getPhotoUrl();

            }

            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {}
    }
**/
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(AdminSignUpActivity.this, AdminHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "User is already register", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            //updateUI(null);
                        }
                    }
                });
    }


    private void userRegister() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPass.getText().toString().trim();
        //checking the validity of the email
        if(email.isEmpty()){
            signUpEmail.setError("Enter an email address");
            signUpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmail.setError("Enter a valid email address");
            signUpEmail.requestFocus();
            return;
        }

        //checking the validity of the email
        if(password.isEmpty()){
            signUpPass.setError("Enter a password");
            signUpPass.requestFocus();
            return;
        }
        if(password.length()<6){
            signUpPass.setError("Minimum length of a password should be 6");
            signUpPass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AdminSignUpActivity.this, "Please Check Your Email For verification.", Toast.LENGTH_SHORT).show();
                                signUpEmail.setText("");
                                signUpPass.setText("");
                            }else {
                                Toast.makeText(AdminSignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            }
                    });
                    /**User user = new User(signUpEmail.getText().toString(), signUpPass.getText().toString());
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Teachers").child(id).setValue(user);
                    Intent intentUp = new Intent(AdminSignUpActivity.this, AdminHomeActivity.class);
                    intentUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentUp);*/
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User is already register", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}