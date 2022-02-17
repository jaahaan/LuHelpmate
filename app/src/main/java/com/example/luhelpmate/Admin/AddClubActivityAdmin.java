package com.example.luhelpmate.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luhelpmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class AddClubActivityAdmin extends AppCompatActivity {

    private CardView pdf;
    private ImageView addCrImg;
    private final int REQ = 100;
    private Uri pdfData = null;
    private Bitmap bitmap = null;
    private EditText clubname;
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
        setContentView(R.layout.activity_add_club);
    }
}