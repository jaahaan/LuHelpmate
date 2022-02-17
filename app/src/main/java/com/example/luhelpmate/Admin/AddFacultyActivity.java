package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFacultyActivity extends AppCompatActivity {

    private CircleImageView addFacultyImg;
    private EditText addFacultyName, addFacultyPhn, addFacultyEmail, addFacultyInitial;
    private Spinner addFacultyDesignation;
    private Button update;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;
    Pattern p = Pattern.compile("[0][1][0-9]{9}");
    String name, initial, designation, phone, email, downloadUrl = "", no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addFacultyImg = findViewById(R.id.addFacultyImg);
        addFacultyName = findViewById(R.id.addfacultyName);
        addFacultyInitial = findViewById(R.id.addFacultyAcronym);
        addFacultyPhn = findViewById(R.id.addfacultyPhn);
        addFacultyEmail = findViewById(R.id.addfacultyEmail);
        addFacultyDesignation = findViewById(R.id.designation);
        update = findViewById(R.id.addFaculty);

        String[] items = new String[]{"Designation", "Head", "Professor", "Associate Professor", "Assistant Professor", "Lecturer",};
        addFacultyDesignation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items));
        addFacultyDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = addFacultyDesignation.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addFacultyImg.setOnClickListener(v -> {
            if (hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ);
            }

        });

        update.setOnClickListener(v -> checkValidation());
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
        name = addFacultyName.getText().toString();
        initial = addFacultyInitial.getText().toString();
        phone = addFacultyPhn.getText().toString();
        email = addFacultyEmail.getText().toString();
        if (name.isEmpty()) {
            addFacultyName.setError("Empty");
            addFacultyName.requestFocus();
        } else if (initial.isEmpty()) {
            addFacultyInitial.setError("Empty");
            addFacultyInitial.requestFocus();
        } else if (designation.equals("Designation")) {
            Toast.makeText(AddFacultyActivity.this, "Please Provide Designation", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            addFacultyPhn.setError("Empty");
            addFacultyPhn.requestFocus();
        } else if (!p.matcher(phone).matches()) {
            addFacultyPhn.setError("Enter a valid Mobile Number");
            addFacultyPhn.requestFocus();
        } else if (email.isEmpty()) {
            addFacultyEmail.setError("Empty");
            addFacultyEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            addFacultyEmail.setError("Enter a valid email address");
            addFacultyEmail.requestFocus();
        } else if (bitmap == null) {
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
        } else {
            pd.setMessage("Updating...");
            pd.show();
            uploadImage();
        }
    }


    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Faculty").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddFacultyActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData();
                }));
            } else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadData() {
        switch (designation) {
            case "Head":
                no = "a";
                break;
            case "Professor":
                no = "b";
                break;
            case "Associate Professor":
                no = "c";
                break;
            case "Assistant Professor":
                no = "d";
                break;
            case "Lecturer":
                no = "e";
                break;
        }

        String uniqueKey = databaseReference.push().getKey();
        FacultyData facultyData = new FacultyData(name + " ", initial, designation, phone, email, downloadUrl, uniqueKey, no);

        databaseReference.child(uniqueKey).setValue(facultyData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(AddFacultyActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            /**addFacultyName.setText("");
             addFacultyInitial.setText("");
             addFacultyEmail.setText("");
             addFacultyPhn.setText("");
             addFacultyImg.setImageResource(R.drawable.download);*/

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(this, FacultyActivityAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Faculty Notification")
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle("New Faculty Member")
                    .setContentText("Name: " + name).setStyle(new NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel("Faculty Notification", "Faculty Notification", importance);
                builder.setChannelId("Faculty Notification");
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(),
                    builder.build());

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            addFacultyImg.setImageBitmap(bitmap);
        }
    }
}
