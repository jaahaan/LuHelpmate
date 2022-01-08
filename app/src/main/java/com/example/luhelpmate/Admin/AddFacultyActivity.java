package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFacultyActivity extends AppCompatActivity {

    private CircleImageView addFacultyImg;
    private EditText addFacultyName, addFacultyPhn, addFacultyEmail, addFacultyInitial;
    private Spinner addFacultyDesignation;
    private Button update;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog pd;
     String name, acronym, designation, phone, email, downloadUrl = "";

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

        String[] items = new String[]{"Designation", "Head", "Professor", "Associate Professor", "Assistant Professor", "Lecturer", };
        addFacultyDesignation.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));
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
            openGallery();
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addFacultyName.getText().toString();
        acronym = addFacultyInitial.getText().toString();
        phone = addFacultyPhn.getText().toString();
        email = addFacultyEmail.getText().toString();
        if(name.isEmpty()){
            addFacultyName.setError("Empty");
            addFacultyName.requestFocus();
        }
        else if(acronym.isEmpty()){
            addFacultyInitial.setError("Empty");
            addFacultyInitial.requestFocus();
        }
        else if(designation.equals("Designation")){
            Toast.makeText(AddFacultyActivity.this, "Please Provide Designation", Toast.LENGTH_SHORT).show();
        }
        else if(phone.isEmpty()){
            addFacultyPhn.setError("Empty");
            addFacultyPhn.requestFocus();
        }
        else if (phone.length() != 11) {
            addFacultyPhn.setError("Enter a valid Mobile Number");
            addFacultyPhn.requestFocus();
        }
        else if(email.isEmpty()){
            addFacultyEmail.setError("Empty");
            addFacultyEmail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            addFacultyEmail.setError("Enter a valid email address");
            addFacultyEmail.requestFocus();
        }
        else if(bitmap == null){
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
        }
        else {
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
        filePath = storageReference.child("Faculty").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddFacultyActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(AddFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        final String uniqueKey = databaseReference.push().getKey();

        FacultyData facultyData = new FacultyData(name, "("+acronym+")", designation, "Contact: "+phone, "Email: "+email, downloadUrl, uniqueKey);

        databaseReference.child(uniqueKey).setValue(facultyData).addOnSuccessListener(unused -> {
            addFacultyImg.setImageBitmap(null);
            pd.dismiss();
            Toast.makeText(AddFacultyActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            addFacultyName.setText("");
            addFacultyInitial.setText("");
            addFacultyEmail.setText("");
            addFacultyPhn.setText("");
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddFacultyActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
