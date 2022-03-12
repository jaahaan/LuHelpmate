package com.example.luhelpmate.Question;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EditQuestion extends AppCompatActivity implements View.OnClickListener {

    private ImageView img, pdf;
    private PhotoView previewImg;
    private final int REQ = 1;
    private Bitmap bitmap;
    private Uri pdfData = null;
    TextView pdfText;
    String pdfName;
    private AutoCompleteTextView exam, session, code, title, initial;
    private String editExam, editSession, editCode, editTitle, editInitial, editpdf, editImg, imgUrl = "", pdfUrl = "";

    DatabaseReference eRef, cRef, tRef, reference, dbRef;
    private StorageReference storageReference;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        cRef = FirebaseDatabase.getInstance().getReference().child("Course List");
        tRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        eRef = FirebaseDatabase.getInstance().getReference().child("exam");
        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchCourse(snapshot);
                //searchTitle(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchTeacher(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        eRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ses(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Questions");
        storageReference = FirebaseStorage.getInstance().getReference();

        editExam = getIntent().getStringExtra("exam");
        editSession = getIntent().getStringExtra("session");
        editTitle = getIntent().getStringExtra("title");
        editCode = getIntent().getStringExtra("code");
        editInitial = getIntent().getStringExtra("initial");
        editpdf = getIntent().getStringExtra("pdf");
        editImg = getIntent().getStringExtra("image");

        pd = new ProgressDialog(this);
        img = findViewById(R.id.img);
        pdf = findViewById(R.id.pdf);
        pdfText = findViewById(R.id.pdfName);

        exam = findViewById(R.id.exam);
        session = findViewById(R.id.session);
        code = findViewById(R.id.code);
        title = findViewById(R.id.title);
        initial = findViewById(R.id.teacherInitial);
        Button update = findViewById(R.id.update);
        previewImg = findViewById(R.id.previewImg);

        exam.setText(editExam);
        session.setText(editSession);
        code.setText(editCode);
        title.setText(editTitle);
        initial.setText(editInitial);
        if (!editpdf.equals("")) pdfText.setText(editpdf);
        else pdfText.setText(editImg);

        img.setOnClickListener(this);
        pdf.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    private void ses(DataSnapshot snapshot) {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String exam = dataSnapshot.child("exam").getValue(String.class);
                String session = dataSnapshot.child("session").getValue(String.class);
                list1.add(exam);
                list2.add(session);
            }
            ArrayAdapter examAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list1);
            ArrayAdapter sessionAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list2);
            exam.setAdapter(examAdapter);
            exam.setThreshold(1);
            session.setAdapter(sessionAdapter);
            session.setThreshold(1);
        }
    }

    private void searchTeacher(DataSnapshot snapshot) {
        ArrayList<String> teachers = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String teacherData = dataSnapshot.child("initial").getValue(String.class);
                teachers.add(teacherData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, teachers);
            initial.setAdapter(arrayAdapter);
            initial.setThreshold(1);
        }
    }

    private void searchCourse(DataSnapshot snapshot) {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("code").getValue(String.class);
                String courseTitle = dataSnapshot.child("title").getValue(String.class);
                list1.add(courseCode);
                list2.add(courseTitle);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list1);
            ArrayAdapter titleAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list2);
            code.setAdapter(codeAdapter);
            code.setThreshold(1);
            title.setAdapter(titleAdapter);
            title.setThreshold(1);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.img) {
            openGallery();
        }
        if (v.getId() == R.id.pdf) {
            openPdfDocument();
        }
        if (v.getId() == R.id.update) {

            editExam = exam.getText().toString();
            editSession = session.getText().toString();
            editCode = code.getText().toString();
            editTitle = title.getText().toString();
            editInitial = initial.getText().toString();
            if (!editpdf.equals("")) editpdf = pdfText.getText().toString();
            else editImg = pdfText.getText().toString();

            if (exam.getText().toString().isEmpty()) {
                exam.setError("Empty");
                exam.requestFocus();
                return;
            } else if (session.getText().toString().isEmpty()) {
                session.setError("Empty");
                session.requestFocus();
                return;
            } else if (code.getText().toString().isEmpty()) {
                code.setError("Empty");
                code.requestFocus();
                return;
            } else if (title.getText().toString().isEmpty()) {
                title.setError("Empty");
                title.requestFocus();
                return;
            } else if (bitmap == null && pdfData == null) {
                pd.setMessage("Uploading...");
                pd.show();
                if (!editpdf.equals("")) uploadData(editpdf);
                else uploadData(editImg);
            } else if (bitmap == null) {
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
        StorageReference reference = storageReference.child("QuestionPdf").child(pdfName);
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri uri = uriTask.getResult();
                pdfUrl = String.valueOf(uri);
                uploadData(pdfUrl);
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
        filePath = storageReference.child("Question").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EditQuestion.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    uploadData(imgUrl);
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData(String p) {

        String uniqueKey = getIntent().getStringExtra("key");

        HashMap<String, Object> map = new HashMap<>();
        map.put("exam", editExam);
        map.put("session", editSession);
        map.put("title", editTitle);
        map.put("code", editCode);
        map.put("initial", editInitial);
        if (!editpdf.equals("")) map.put("pdf", p);
        else map.put("img", p);

        map.put("key", uniqueKey);
        reference.child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PreviousQuestionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            previewImg.setImageBitmap(bitmap);

            pdfText.setText(pdfName);
        }
    }
}