package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText forgotEmail;
    private Button button;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgotEmail = findViewById(R.id.email);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = forgotEmail.getText().toString();
                if (email.isEmpty()){
                    forgotEmail.setError("Enter an email address");
                    forgotEmail.requestFocus();
                }
                else fogotPassword();
            }
        });
    }

    private void fogotPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Check Your Email.", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}