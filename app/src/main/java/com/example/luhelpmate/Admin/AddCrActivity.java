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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.Data.CrData;
import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.regex.*;

public class AddCrActivity extends AppCompatActivity {

    private ImageView addCrImg;
    private EditText addCrName, addCrPhn, addCrEmail, addCrId;
    private Spinner addBatch, addSection;
    private Button updateCr;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    private ProgressDialog pd;
    Pattern p = Pattern.compile("[0][1][0-9]{9}");
    String batch, section, name, id, phone, email, downloadUrl = "";

    String[] itemsBatch = new String[]{"Batch", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58"};
    String[] itemsSection = new String[]{"Section", "A", "B", "C", "D", "E", "F", "G", "H"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cr);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cr Info");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addBatch = findViewById(R.id.addCrBatch);
        addSection = findViewById(R.id.addCrSection);
        addCrImg = findViewById(R.id.addCrImg);
        addCrName = findViewById(R.id.addCrName);
        addCrId = findViewById(R.id.addCrSId);
        addCrPhn = findViewById(R.id.addCrPhn);
        addCrEmail = findViewById(R.id.addCrEmail);
        updateCr = findViewById(R.id.crUpdate);

        addBatch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsBatch));
        addBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batch = addBatch.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addSection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsSection));
        addSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = addSection.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addCrImg.setOnClickListener(v -> {
            if (hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ);
            }
        });
        updateCr.setOnClickListener(v -> checkValidation());
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

        name = addCrName.getText().toString();
        id = addCrId.getText().toString();
        phone = addCrPhn.getText().toString();
        email = addCrEmail.getText().toString();

        if (batch.equals("Batch")) {
            Toast.makeText(AddCrActivity.this, "Please Provide Batch", Toast.LENGTH_SHORT).show();
        } else if (section.equals("Section")) {
            section = "-";
        } else if (name.isEmpty()) {
            addCrName.setError("Empty");
            addCrName.requestFocus();
        } else if (id.isEmpty()) {
            addCrId.setError("Empty");
            addCrId.requestFocus();
        } else if (phone.isEmpty()) {
            addCrPhn.setError("Empty");
            addCrPhn.requestFocus();
        } else if (!p.matcher(phone).matches()) {
            addCrPhn.setError("Enter a valid Mobile Number");
            addCrPhn.requestFocus();
        } else if (email.isEmpty()) {
            addCrEmail.setError("Empty");
            addCrEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            addCrEmail.setError("Enter a valid email address");
            addCrEmail.requestFocus();
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Cr").child(finalimg + "png");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddCrActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
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
                    Toast.makeText(AddCrActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        final String uniqueKey = databaseReference.push().getKey();

        CrData crData = new CrData(batch, section, name, id, phone, email, downloadUrl, uniqueKey);

        databaseReference.child(uniqueKey).setValue(crData).addOnSuccessListener(unused -> {

            pd.dismiss();
            Toast.makeText(AddCrActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            /**addCrImg.setImageResource(R.drawable.download);
            addCrName.setText("");
            addCrId.setText("");
            addCrPhn.setText("");
            addCrEmail.setText("");*/

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(this, CrActivityAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Cr Notification")
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle("New CR")
                    .setContentText("Name: " +name).setStyle(new NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context. NOTIFICATION_SERVICE ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel( "Cr Notification" , "Cr Notification" , importance) ;
                builder.setChannelId( "Cr Notification" ) ;
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel) ;
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                    builder.build()) ;

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddCrActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            addCrImg.setImageBitmap(bitmap);
        }
    }
}