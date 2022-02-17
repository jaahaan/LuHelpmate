package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FacultyEditActivity extends AppCompatActivity {

    private ImageView upImage;
    private EditText upName, upInitial, upPhone, upEmail, upDesignation;
    private Button update;

    private String name, initial, designation, phone, email, image;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    Pattern p = Pattern.compile("[0][1][0-9]{9}");

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private String downloadUrl;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_edit);

        pd = new ProgressDialog(this);

        name = getIntent().getStringExtra("name");
        initial = getIntent().getStringExtra("initial");
        designation = getIntent().getStringExtra("designation");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        upName = findViewById(R.id.updatefacultyName);
        upInitial = findViewById(R.id.updateFacultyinitial);
        upDesignation = findViewById(R.id.updateDesignation);
        upPhone = findViewById(R.id.updatefacultyPhn);
        upEmail = findViewById(R.id.updatefacultyEmail);
        upImage = findViewById(R.id.updateFacultyImg);
        update = findViewById(R.id.updateFaculty);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        storageReference = FirebaseStorage.getInstance().getReference();

        Glide.with(this).load(image).into(upImage);

        upName.setText(name);
        upInitial.setText(initial);
        upDesignation.setText(designation);
        upPhone.setText(phone);
        upEmail.setText(email);

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = upName.getText().toString();
                initial = upInitial.getText().toString();
                designation = upDesignation.getText().toString();
                phone = upPhone.getText().toString();
                email = upEmail.getText().toString();
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        if (name.isEmpty()) {
            upName.setError("Empty");
            upName.requestFocus();
        } else if (initial.isEmpty()) {
            upInitial.setError("Empty");
            upInitial.requestFocus();
        } else if (designation.isEmpty()) {
            upDesignation.setError("Empty");
            upDesignation.requestFocus();
        } else if (phone.isEmpty()) {
            upPhone.setError("Empty");
            upPhone.requestFocus();
        } else if (!p.matcher(phone).matches()) {
            upPhone.setError("Enter a valid Mobile Number");
            upPhone.requestFocus();
        } else if (email.isEmpty()) {
            upEmail.setError("Empty");
            upEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            upEmail.setError("Enter a valid email address");
            upEmail.requestFocus();
        } else if (bitmap == null) {
            pd.setMessage("Updating...");
            pd.show();
            uploadData(image);
        } else {
            pd.setMessage("Updating...");
            pd.show();
            uploadImage();
        }

    }

    private void uploadData(String s) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("initial", initial);
        map.put("designation", designation);
        map.put("phone", phone);
        map.put("email", email);
        map.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        databaseReference.child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Updated Successfully" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FacultyActivityAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(FacultyEditActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData(downloadUrl);
                }));
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

}