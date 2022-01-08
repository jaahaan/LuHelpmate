package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luhelpmate.Data.NoticeData;
import com.example.luhelpmate.R;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView addNoticeImg;
    private ImageView noticeImg;
    private final int REQ=1;
    private Bitmap bitmap;
    private EditText noticeTitle;
    private Button updateButton;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        addNoticeImg = findViewById(R.id.addnoticeImg);
        noticeTitle = findViewById(R.id.noticeTitle);
        updateButton = findViewById(R.id.noticeUpdate);
        noticeImg = findViewById(R.id.noticeImg);

        addNoticeImg.setOnClickListener(this);
        noticeTitle.setOnClickListener(this);
        updateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addnoticeImg){
            openGallery();
        }
        if(v.getId() == R.id.noticeUpdate){
            if(noticeTitle.getText().toString().isEmpty()){
                noticeTitle.setError("Empty");
                noticeTitle.requestFocus();
            } else if(bitmap == null){
                pd.setMessage("Uploading...");
                pd.show();
                uploadData();
            } else {
                pd.setMessage("Uploading...");
                pd.show();
                uploadImage();
            }
        }

    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Notice").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UpdateNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        dbRef = databaseReference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();

        String title = noticeTitle.getText().toString();
        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(forDate.getTime());

        Calendar forTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(forTime.getTime());

        NoticeData noticeData = new NoticeData(title, downloadUrl, date, time, uniqueKey);

        dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //noticeImg.setImageBitmap(null);
                pd.dismiss();
                Toast.makeText(UpdateNoticeActivity.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQ && resultCode== RESULT_OK){
            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImg.setImageBitmap(bitmap);
        }
    }
}