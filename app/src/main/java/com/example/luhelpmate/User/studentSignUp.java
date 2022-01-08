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
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class studentSignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText signUpEmail, signUpPass;
    private Button signUpButton;
    private TextView login;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPass = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();

        login.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signUpButton) {
            userRegister();
        }
        else if(v.getId()==R.id.login) {
            Intent intent = new Intent(studentSignUp.this, studentSignIn.class);
            startActivity(intent);
        }
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
                                Toast.makeText(studentSignUp.this, "Please Check Your Email For verification.", Toast.LENGTH_SHORT).show();
                                signUpEmail.setText("");
                                signUpPass.setText("");
                            }else {
                                Toast.makeText(studentSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    /**finish();
                    User user = new User(signUpEmail.getText().toString(), signUpPass.getText().toString());
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Students").child(id).setValue(user);
                    Intent intentUp = new Intent(studentSignUp.this, HomeActivity.class);
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