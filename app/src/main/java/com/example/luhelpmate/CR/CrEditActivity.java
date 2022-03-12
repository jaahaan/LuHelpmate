package com.example.luhelpmate.CR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class CrEditActivity extends AppCompatActivity {
    private ImageView upImage;
    private EditText upName, upBatch, upSection, upPhone, upEmail;
    private Button update;

    private String name, batch, section, phone, email, image;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    Pattern p = Pattern.compile("([+][8][8])?[0][ ]?[1][0-9]{3}[-]?[0-9]{6}");
    Pattern n = Pattern.compile("[a-z A-Z.]*");

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private String downloadUrl;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cr_edit);

        pd = new ProgressDialog(this);

        name = getIntent().getStringExtra("name");
        batch = getIntent().getStringExtra("batch");
        section = getIntent().getStringExtra("section");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        upName = findViewById(R.id.upCrName);
        upBatch = findViewById(R.id.upCrBatch);
        upSection = findViewById(R.id.upCrSection);
        upPhone = findViewById(R.id.upCrPhn);
        upEmail = findViewById(R.id.upCrEmail);
        upImage = findViewById(R.id.upCrImg);
        update = findViewById(R.id.update);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cr Info");
        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            Picasso.get().load(image).into(upImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        upName.setText(name);
        upBatch.setText(batch);
        upSection.setText(section);
        upPhone.setText(phone);
        upEmail.setText(email);

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(CrEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ);
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = upName.getText().toString().trim();
                batch = upBatch.getText().toString().trim();
                section = upSection.getText().toString().trim();
                phone = upPhone.getText().toString().trim();
                email = upEmail.getText().toString().trim();
                checkValidation();
            }
        });
    }

    private boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void checkValidation() {
        if (name.isEmpty()) {
            upName.setError("Empty");
            upName.requestFocus();
        } else if (!n.matcher(name).matches()) {
            upName.setError("Name can be only Alphabet");
            upName.requestFocus();
        } else if (batch.isEmpty()) {
            upBatch.setError("Empty");
            upBatch.requestFocus();
        } else if (section.isEmpty()) {
            section = "-";
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
        map.put("batch", batch);
        map.put("section", section);
        map.put("phone", phone);
        map.put("email", email);
        map.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        map.put("key", uniqueKey);

        databaseReference.child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Updated Successfully" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CrActivityAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong!!!" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Cr").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(CrEditActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData(downloadUrl);
                }));
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception e) {
                Toast.makeText(this, "Out Of Memory. Please Choose another One", Toast.LENGTH_SHORT).show();
            }
            upImage.setImageBitmap(bitmap);
        }
    }

}