package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.printservice.PrintDocument;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.AdminAdapter.CourseListAdapter;
import com.example.luhelpmate.Data.CourseData;

import com.example.luhelpmate.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class CourseList extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<CourseData> list;
    private CourseListAdapter adapter;
    private DatabaseReference reference;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");

        recyclerView = findViewById(R.id.courseRecycler);
        progressBar = findViewById(R.id.progressBar);
        TextView ct = findViewById(R.id.ct);
        ct.setText("   ");

        Query query = reference.orderByChild("code");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                if (!snapshot.exists()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseData data = snapshot1.getValue(CourseData.class);
                        list.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseListAdapter(list, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddCourseActivity.class)));

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Course");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<CourseData> newList = new ArrayList<>();
                for (CourseData data : list) {
                    String query1 = data.getTitle().toLowerCase();
                    String query2 = data.getCode().toLowerCase();
                    if (query1.contains(newText)) {
                        newList.add(data);
                    }
                    else if (query2.contains(newText)) {
                        newList.add(data);
                    }
                }
                adapter.setFilter(newList);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}