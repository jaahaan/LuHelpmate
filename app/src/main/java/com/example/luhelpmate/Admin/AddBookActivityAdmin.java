package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Data.BookData;
import com.example.luhelpmate.Data.CrData;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AddBookActivityAdmin extends AppCompatActivity implements View.OnClickListener {

    private CardView pdf;
    private final int REQ = 100;
    private Uri pdfData = null;
    private EditText bname, bauthor, bedition, bcode;
    TextView pdfText;
    String pdfName;

    private Button update;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    String bookname, author, edition, code, downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        bname = findViewById(R.id.bookName);
        bauthor = findViewById(R.id.authorName);
        bedition = findViewById(R.id.edition);
        bcode = findViewById(R.id.code);
        update = findViewById(R.id.update);
        pdfText = findViewById(R.id.pdfName);
        pdf = findViewById(R.id.pdf);

        pdf.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pdf) {
            openGallery();
        }
        if (v.getId() == R.id.update) {
            bookname = bname.getText().toString();
            author = bauthor.getText().toString();
            edition = bedition.getText().toString();
            code = bcode.getText().toString();
            if (bookname.isEmpty()) {
                bname.setError("Empty");
                bname.requestFocus();
            } else if (author.isEmpty()) {
                bauthor.setError("Empty");
                bauthor.requestFocus();
            } else if (edition.isEmpty()) {
                bedition.setError("Empty");
                bedition.requestFocus();
            } else if (pdfData == null) {
                pd.setMessage("Uploading...");
                pd.show();
                uploadData();
            } else {
                pd.setMessage("Uploading...");
                pd.show();
                uploadPdf();
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
                downloadUrl = String.valueOf(uri);
                uploadData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        final String uniqueKey = databaseReference.push().getKey();

        BookData bookData = new BookData(bookname, author, edition, code, downloadUrl, uniqueKey);

        databaseReference.child(uniqueKey).setValue(bookData).addOnSuccessListener(unused -> {

            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(getApplicationContext(), BookActivityAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Book Notification")
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle("New Book")
                    .setContentText("Name: " + bookname).setStyle(new NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel("Book Notification", "Book Notification", importance);
                builder.setChannelId("Book Notification");
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(),
                    builder.build());

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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