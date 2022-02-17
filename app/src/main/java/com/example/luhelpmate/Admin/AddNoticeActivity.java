package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Data.NoticeData;
import com.example.luhelpmate.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img, pdf;
    private PhotoView noticeImg;
    private final int REQ = 1;
    private Bitmap bitmap;
    private Uri pdfData = null;
    TextView pdfText;
    String pdfName;
    private EditText noticeTitle;
    private Button updateButton;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    String imgUrl = "";
    String pdfUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        img = findViewById(R.id.img);
        pdf = findViewById(R.id.pdf);
        pdfText = findViewById(R.id.pdfName);
        noticeTitle = findViewById(R.id.noticeTitle);
        updateButton = findViewById(R.id.noticeUpdate);
        noticeImg = findViewById(R.id.noticeImg);

        img.setOnClickListener(this);
        pdf.setOnClickListener(this);
        updateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img) {
            openGallery();
        } if (v.getId() == R.id.pdf) {
            openPdfDocument();
        } if (v.getId() == R.id.noticeUpdate) {

            if (noticeTitle.getText().toString().isEmpty()) {
                noticeTitle.setError("Empty");
                noticeTitle.requestFocus();
            } else if (bitmap == null && pdfData== null) {
                pd.setMessage("Uploading...");
                pd.show();
                uploadData();
            }
            else if (bitmap == null){
                pd.setMessage("Uploading...");
                pd.show();
                uploadPdf();
            } else {
                pd.setMessage("Uploading...");
                pd.show();
                uploadImage();
            }
        }
    }

    private void uploadPdf() {
        StorageReference reference = storageReference.child("BookPdf").child(pdfName + "-" + System.currentTimeMillis() + ".pdf");
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri uri = uriTask.getResult();
                pdfUrl = String.valueOf(uri);
                uploadData();
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
        filePath = storageReference.child("Notice").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddNoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                    Toast.makeText(AddNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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

        NoticeData noticeData = new NoticeData(title, imgUrl, pdfUrl, date, time, uniqueKey);

        dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddNoticeActivity.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), NoticeActivityAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Notice Notification")
                        .setSmallIcon(R.drawable.splashicon)
                        .setContentTitle("New Notice")
                        .setContentText("Notice Title: " + title).setStyle(new NotificationCompat.BigTextStyle())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent).setAutoCancel(true);

                NotificationManager mNotificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel("Notice Notification", "Notice Notification", importance);
                    builder.setChannelId("Notice Notification");
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                assert mNotificationManager != null;
                mNotificationManager.notify((int) System.currentTimeMillis(),
                        builder.build());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddNoticeActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPdfDocument() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ);
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
            noticeImg.setImageBitmap(bitmap);
        }
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();
            if (pdfData.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (pdfData.toString().startsWith("file://")) {
                pdfName = new File(pdfData.toString()).getName();
            }
            pdfText.setText(pdfName);
        }
    }
}