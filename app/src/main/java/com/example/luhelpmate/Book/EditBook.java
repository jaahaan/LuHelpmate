package com.example.luhelpmate.Book;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class EditBook extends AppCompatActivity implements View.OnClickListener {

    private final int REQ = 100;
    private Uri pdfData = null;
    private EditText bname, bauthor, bedition, bcode;
    TextView pdfText;
    String pdfName;
    Pattern n = Pattern.compile("[\\D]*");

    private DatabaseReference databaseReference, dbRef;
    private StorageReference storageReference;
    String name, author, edition, code, pdf, downloadUrl="";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);


        name = getIntent().getStringExtra("bookName");
        author = getIntent().getStringExtra("author");
        edition = getIntent().getStringExtra("edition");
        code = getIntent().getStringExtra("code");
        pdf = getIntent().getStringExtra("pdf");

        bname = findViewById(R.id.bookName);
        bauthor = findViewById(R.id.authorName);
        bedition = findViewById(R.id.edition);
        bcode = findViewById(R.id.code);
        Button update = findViewById(R.id.update);
        pdfText = findViewById(R.id.pdfName);
        CardView pdf = findViewById(R.id.pdf);

        bname.setText(name);
        bauthor.setText(author);
        bedition.setText(edition);
        bcode.setText(code);
        pdfText.setText(this.pdf);

        pdf.setOnClickListener(this);
        update.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pdf) {
            openGallery();
        }
        if (v.getId() == R.id.update) {
            name = bname.getText().toString();
            author = bauthor.getText().toString();
            edition = bedition.getText().toString();
            code = bcode.getText().toString();
            pdf = pdfText.getText().toString();

            if (name.isEmpty()) {
                bname.setError("Empty");
                bname.requestFocus();
                return;
            } else if (author.isEmpty()) {
                bauthor.setError("Empty");
                bauthor.requestFocus();
                return;
            } else if (!n.matcher(author).matches()) {
                bauthor.setError("Name can't be digit");
                bauthor.requestFocus();
            } else if (edition.isEmpty()) {
                bedition.setError("Empty");
                bedition.requestFocus();
                return;
            } else if (pdfData == null) {
                pd.setMessage("Uploading...");
                pd.show();
                uploadData(pdf);
            } else {
                pd.setMessage("Uploading...");
                pd.show();
                uploadPdf();
            }
        }
    }

    private void uploadPdf() {
        StorageReference reference = storageReference.child("BookPdf").child(pdfName);
        reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri uri = uriTask.getResult();
                downloadUrl = String.valueOf(uri);
                uploadData(downloadUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String p) {

        String uniqueKey = getIntent().getStringExtra("key");

        HashMap<String, Object> map = new HashMap<>();
        map.put("bookName", name);
        map.put("author", author);
        map.put("edition", edition);
        map.put("code", code);
        map.put("pdf", p);
        map.put("key", uniqueKey);
        databaseReference.child(uniqueKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Updated Successfully" , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BookActivityAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong" , Toast.LENGTH_SHORT).show();
            }
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
