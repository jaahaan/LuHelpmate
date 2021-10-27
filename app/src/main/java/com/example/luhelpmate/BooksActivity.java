package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        ArrayList<Object> objects = new ArrayList<>();


        objects.add(new Object("Introduction to Computers", "Author: Muhammad Alamgir", "ELEVENTH EDITION", "CSE-1111", ""));
        objects.add(new Object("Python Porichiti", "Author: Tamim Shahriar Subeen", "BENGALI EDITION", "CSE-1112", ""));
        objects.add(new Object("Computer Programming", "Author: Tamim Shahriar Subeen", "SECOND EDITION", "CSE-1213", "https://drive.google.com/file/d/1Jc6TK--2Xwu_lpuhxk2hR0BYoGs9CTwM/view?usp=sharing"));
        objects.add(new Object("Data Structures", "Author: Seymour Lipschutz", "SPECIAL INDIAN EDITION", "CSE-1315", ""));
        objects.add(new Object("Data And Computer Communication", "Author: William Stallings", "EIGHTH EDITION", "CSE-2321", "https://drive.google.com/file/d/1GfODpUwjGlskqs6Q5EWPursukg8S17Z3/view?usp=sharing"));
        objects.add(new Object("Computer System Architecture", "Author: M. Morris Mano", "THIRD EDITION", "CSE-3117", "https://drive.google.com/file/d/1yI2uoygvdHd7fDBZSJAFsTQ7pj9ZjBD7/view?usp=sharing"));
        objects.add(new Object("Digital Systems Principles and Applications", "Author: Ronald J. Tocci, Neal S. Widmer & Gregory L. Moss", "TENTH EDITION", "EEE-2317", "https://drive.google.com/file/d/1xtfZCznHlkJcSskEVR6qZCEATpwpLaiL/view?usp=sharing"));
        objects.add(new Object("Java The Complete Reference", "Author: Herbert Schildt", "NINTH EDITION", "CSE-3317", "https://drive.google.com/file/d/1E8RmjofLpBCoj8pVbZ6tDV54SPKUExqn/view?usp=sharing"));
        objects.add(new Object("BookName", "Author: ", "THIRD EDITION", "CSE-3117", ""));
        ListView bookListView = findViewById(R.id.list);

        // Create a new adapter that takes the list of books as input
        final BookAdapter adapter = new BookAdapter(this, objects);

        // Set the adapter
        // so the list can be populated in the user interface
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current book that was clicked on
                Object currentbook = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentbook.getbUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                try {
                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(BooksActivity.this, getString(R.string.toast3), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}