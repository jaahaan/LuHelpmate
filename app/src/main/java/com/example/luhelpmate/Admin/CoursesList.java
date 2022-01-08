package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Data.CourseData;

import com.example.luhelpmate.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collection;

public class CoursesList extends AppCompatActivity {

    private RecyclerView courseListRecyclerView;
    private DatabaseReference reference;

    TextView addNewCourse;

    CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_list);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");

        courseListRecyclerView = findViewById(R.id.courseRecycler);
        addNewCourse = findViewById(R.id.addNewCourse);

        addNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(CoursesList.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });

        courseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<CourseData> options = new FirebaseRecyclerOptions.Builder<CourseData>().setQuery(FirebaseDatabase.getInstance().getReference().child("Course List"), CourseData.class).build();
        adapter = new CourseListAdapter(options);
        courseListRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void txtSearch(String str){
        FirebaseRecyclerOptions<CourseData> options = new FirebaseRecyclerOptions.Builder<CourseData>().setQuery(FirebaseDatabase.getInstance().getReference().child("Course List").orderByChild("title").startAt(str).endAt(str+"-"), CourseData.class).build();
        adapter = new CourseListAdapter(options);
        adapter.startListening();
        courseListRecyclerView.setAdapter(adapter);
        }

}