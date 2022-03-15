package com.example.luhelpmate.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EditProfileTeacher extends AppCompatActivity {

    private ImageView image;
    private EditText addName, addInitial, addDepartment, addEmail;
    private AutoCompleteTextView addDesignation;
    private String name, initial, designation, department, email, admin;
    private Button update;
    private Pattern n = Pattern.compile("[a-z A-Z.]*");
    ProgressDialog pd;

    FirebaseFirestore firestore;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_teacher);

        pd = new ProgressDialog(this);

        image = findViewById(R.id.image);
        addName = findViewById(R.id.name);
        addInitial = findViewById(R.id.initial);
        addDesignation = findViewById(R.id.designation);
        addDepartment = findViewById(R.id.department);
        addEmail = findViewById(R.id.email);
        update = findViewById(R.id.update);
        progressBar = findViewById(R.id.progressBar);
        firestore = FirebaseFirestore.getInstance();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri uri = account.getPhotoUrl();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Designation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchDesignation(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    addName.setText(value.getString("name"));
                    Glide.with(getApplicationContext()).load(uri).circleCrop().into(image);
                    addEmail.setText(value.getString("email"));
                    addInitial.setText(value.getString("initial"));
                    addDesignation.setText(value.getString("designation"));
                    addDepartment.setText(value.getString("department"));
                    admin = value.getString("admin");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = addName.getText().toString().trim();
                initial = addInitial.getText().toString().trim();
                designation = addDesignation.getText().toString().trim();
                department = addDepartment.getText().toString().trim();
                email = addEmail.getText().toString().trim();
                checkValidation();
            }
        });

    }

    private void searchDesignation(DataSnapshot snapshot) {
        ArrayList<String> list = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("designation").getValue(String.class);
                list.add(data);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
            addDesignation.setAdapter(arrayAdapter);
            addDesignation.setThreshold(1);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkValidation() {
        if (name.isEmpty()) {
            addName.setError("Empty");
            addName.requestFocus();
        } else if (!n.matcher(name).matches()) {
            addName.setError("Name can be only Alphabet");
            addName.requestFocus();
        } else if (initial.isEmpty()) {
            addInitial.setError("Empty");
            addInitial.requestFocus();
        } else if (!n.matcher(initial).matches()) {
            addName.setError("Initial can be only Alphabet");
            addName.requestFocus();
        } else if (designation.isEmpty()) {
            addDesignation.setError("Empty");
            addDesignation.requestFocus();
        } else if (department.isEmpty()) {
            addDepartment.setError("Empty");
            addDepartment.requestFocus();
        } else if (email.isEmpty()) {
            addEmail.setError("Empty");
            addEmail.requestFocus();
        } else if (!email.equals(GoogleSignIn.getLastSignedInAccount(this).getEmail())) {
            addEmail.setError("You Can't Change Your Email");
            addEmail.requestFocus();
        } else {
            pd.setMessage("Updating...");
            pd.show();
            updateInfo();
        }

    }

    private void updateInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference df = firestore.collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    DocumentReference df = firestore.collection("Users").document(user.getUid());
                    Map<String, String> userInfo = new HashMap<>();
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    userInfo.put("email", email);
                    userInfo.put("image", signInAccount.getPhotoUrl().toString());
                    userInfo.put("name", name);
                    userInfo.put("designation", designation);
                    userInfo.put("initial", initial);
                    userInfo.put("department", department);
                    userInfo.put("admin", admin);
                    userInfo.put("uid", user.getUid());
                    df.set(userInfo);
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Info Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
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