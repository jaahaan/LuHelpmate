package com.example.luhelpmate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.R;
import com.example.luhelpmate.UserAdapter.CourseListAdapterUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseListUser extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<CourseData> list;
    private CourseListAdapterUser adapter;
    private DatabaseReference reference;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");

        recyclerView = findViewById(R.id.courseRecycler);
        progressBar = findViewById(R.id.progressBar);


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
                    adapter = new CourseListAdapterUser(list, getApplicationContext());
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
        fab.setVisibility(View.GONE);
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