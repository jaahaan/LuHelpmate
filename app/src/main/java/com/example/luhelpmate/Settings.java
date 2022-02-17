package com.example.luhelpmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Admin.BatchListActivity;
import com.example.luhelpmate.Admin.HomeActivityAdmin;
import com.example.luhelpmate.User.BatchListUser;
import com.example.luhelpmate.User.HomeActivityUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private ImageView image;
    private TextView name, initial, designation, email, admin, batchList, manageUser, signOut, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        initial = findViewById(R.id.initial);
        designation = findViewById(R.id.designation);
        email = findViewById(R.id.email);
        admin = findViewById(R.id.admin);
        admin.setVisibility(View.GONE);

        batchList = findViewById(R.id.batchList);
        manageUser = findViewById(R.id.manageUser);
        signOut = findViewById(R.id.signOut);
        share = findViewById(R.id.share);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri uri = account.getPhotoUrl();
        /**try {
         Picasso.get().load(uri).into(image);
         } catch (Exception e) {
         }*/
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    name.setText(value.getString("name"));
                    Glide.with(getApplicationContext()).load(uri).circleCrop().into(image);
                    email.setText(value.getString("email"));
                    initial.setText(value.getString("initial"));
                    designation.setText(value.getString("designation"));
                    if (value.getString("admin").equals("1")) {
                        admin.setVisibility(View.VISIBLE);
                        batchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BatchListActivity.class)));
                        manageUser.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UserInfoActivity.class)));

                    } else if (value.getString("admin").equals("2")) {
                        admin.setVisibility(View.GONE);
                        manageUser.setVisibility(View.GONE);
                        batchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BatchListUser.class)));
                    }

                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LU");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

                    startActivity(Intent.createChooser(shareIntent, "Share With"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}