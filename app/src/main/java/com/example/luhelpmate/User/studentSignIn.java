package com.example.luhelpmate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class studentSignIn extends AppCompatActivity implements View.OnClickListener {

    private EditText signInEmail, signInPass;
    private Button signInButton;
    private Button create;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_in);
        mAuth = FirebaseAuth.getInstance();
        signInEmail = findViewById(R.id.signInEmail);
        signInPass = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        create = findViewById(R.id.createNewAccount);
        progressBar = findViewById(R.id.progressBar);
        signInButton.setOnClickListener(this);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signInButton) {
            userLogin();
        }
        else if(v.getId()==R.id.createNewAccount) {
            Intent intent = new Intent(studentSignIn.this, studentSignUp.class);
            startActivity(intent);
        }
    }

    private void userLogin() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPass.getText().toString().trim();
        //checking the validity of the email
        if (email.isEmpty()) {
            signInEmail.setError("Enter an email address");
            signInEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmail.setError("Enter a valid email address");
            signInEmail.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            signInPass.setError("Enter a password");
            signInPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            signInPass.setError("Minimum length of a password should be 6");
            signInPass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(studentSignIn.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(studentSignIn.this, "Please Verify Your Email Address.", Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(studentSignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}