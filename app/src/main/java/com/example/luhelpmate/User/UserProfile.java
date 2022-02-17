package com.example.luhelpmate.User;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Admin.CrActivityAdmin;
import com.example.luhelpmate.Admin.CrEditActivity;
import com.example.luhelpmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class UserProfile extends AppCompatActivity {

    ImageView imageView;
    EditText sname, semail, sId, sBatch, sSection;
    private String name, id, batch, section, email, image;
    Button button;

    private final int REQ = 1;
    private Bitmap bitmap = null;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private String downloadUrl;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        batch = getIntent().getStringExtra("batch");
        section = getIntent().getStringExtra("section");
        email = getIntent().getStringExtra("email");
        image = getIntent().getStringExtra("image");

        imageView = findViewById(R.id.profileImg);
        semail = findViewById(R.id.sEmail);
        sname = findViewById(R.id.name);
        sId = findViewById(R.id.sId);
        sBatch = findViewById(R.id.sBatch);
        sSection = findViewById(R.id.sSection);
        button = findViewById(R.id.update);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Profile");
        storageReference = FirebaseStorage.getInstance().getReference();


        /**GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        sname.setText(account.getDisplayName());
        semail.setText(account.getEmail());
        Glide.with(this).load(account.getPhotoUrl()).into(imageView);*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openGallery();
                } else {
                    ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null) {
                    pd.setMessage("Updating...");
                    pd.show();
                    uploadData(image);
                } else {
                    pd.setMessage("Updating...");
                    pd.show();
                    uploadImage();
                }

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

    private void uploadData(String s) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", id);
        map.put("batch", batch);
        map.put("section", section);
        map.put("email", email);
        map.put("image", s);

        String uniqueKey = getIntent().getStringExtra("key");
        databaseReference.child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CrActivityAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("User").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UserProfile.this, task -> {
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
            imageView.setImageBitmap(bitmap);
        }
    }

}