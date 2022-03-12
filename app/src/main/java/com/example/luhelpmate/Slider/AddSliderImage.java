package com.example.luhelpmate.Slider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddSliderImage extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;
    private PhotoView previewImage;
    private final int REQ = 1;
    private Bitmap bitmap;
    private Button update;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    String imgUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slider_image);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        image = findViewById(R.id.img);
        update = findViewById(R.id.update);
        previewImage = findViewById(R.id.previewImg);
        image.setOnClickListener(this);
        update.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.img) {
            openGallery();
        } if (v.getId() == R.id.update) {
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "Please Select Image", Toast.LENGTH_SHORT);
                return;
            }
            else {
                pd.setMessage("Uploading...");
                pd.show();
                uploadImage();
            }
        }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            previewImage.setImageBitmap(bitmap);
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        storageReference = storageReference.child("Slider Image").child(finalimg + "jpg");
        final UploadTask uploadTask = storageReference.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddSliderImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imgUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddSliderImage.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadData() {
        dbRef = databaseReference.child("Slider Image");
        final String uniqueKey = dbRef.push().getKey();

        SliderData sliderData = new SliderData(imgUrl, uniqueKey);

        dbRef.child(uniqueKey).setValue(sliderData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddSliderImage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SliderImagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddSliderImage.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}