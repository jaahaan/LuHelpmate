package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.AdminAdapter.CourseListAdapter;
import com.example.luhelpmate.AdminAdapter.CourseOfferingAdapter;
import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.Data.CourseOfferingsData;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseOfferingActivityAdmin extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6;

    private ArrayList<CourseOfferingsData> list1, list2, list3, list4,list5, list6;
    private CourseOfferingAdapter adapter;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private RelativeLayout layout1, layout2, layout3, layout4, layout5, layout6;
    private TextView semester, batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_offering);

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        layout5 = findViewById(R.id.layout5);
        layout6 = findViewById(R.id.layout6);

        //semester = findViewById(R.id.semester);
        batch = findViewById(R.id.batch);

        recyclerView1 = findViewById(R.id.firstRecycler);
        recyclerView2 = findViewById(R.id.secondRecycler);
        recyclerView3 = findViewById(R.id.thirdRecycler);
        recyclerView4 = findViewById(R.id.fourthRecycler);
        recyclerView5 = findViewById(R.id.fifthRecycler);
        recyclerView6 = findViewById(R.id.sixthRecycler);

        reference = FirebaseDatabase.getInstance().getReference().child("Course Offering");
        progressBar = findViewById(R.id.progressBar);
        adapter = new CourseOfferingAdapter(list1, getApplicationContext());

        first();
        second();
        third();
        fourth();
        fifth();
        sixth();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    private void first(){
        reference.child("1st").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout1.setVisibility(View.GONE);
                    recyclerView1.setVisibility(View.GONE);
                } else {
                    layout1.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list1.add(data);
                    }
                    recyclerView1.setHasFixedSize(true);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list1, getApplicationContext());
                    recyclerView1.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void second(){
        reference.child("2nd").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout2.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.GONE);
                } else {
                    layout2.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list2.add(data);
                    }
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list2, getApplicationContext());
                    recyclerView2.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void third(){
        reference.child("3rd").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout3.setVisibility(View.GONE);
                    recyclerView3.setVisibility(View.GONE);
                } else {
                    layout3.setVisibility(View.VISIBLE);
                    recyclerView3.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list3.add(data);
                    }
                    recyclerView3.setHasFixedSize(true);
                    recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list3, getApplicationContext());
                    recyclerView3.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fourth(){
        reference.child("4th").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout4.setVisibility(View.GONE);
                    recyclerView4.setVisibility(View.GONE);
                } else {
                    layout4.setVisibility(View.VISIBLE);
                    recyclerView4.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list4.add(data);
                    }
                    recyclerView4.setHasFixedSize(true);
                    recyclerView4.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list4, getApplicationContext());
                    recyclerView4.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fifth(){
        reference.child("5th").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout5.setVisibility(View.GONE);
                    recyclerView5.setVisibility(View.GONE);
                } else {
                    layout5.setVisibility(View.VISIBLE);
                    recyclerView5.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list5.add(data);
                    }
                    recyclerView5.setHasFixedSize(true);
                    recyclerView5.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list5, getApplicationContext());
                    recyclerView5.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sixth(){
        reference.child("6th").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6 = new ArrayList<>();

                if (!snapshot.exists()) {
                    layout6.setVisibility(View.GONE);
                    recyclerView6.setVisibility(View.GONE);
                } else {
                    layout6.setVisibility(View.VISIBLE);
                    recyclerView6.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseOfferingsData data = snapshot1.getValue(CourseOfferingsData.class);
                        list6.add(data);
                    }
                    recyclerView6.setHasFixedSize(true);
                    recyclerView6.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new CourseOfferingAdapter(list6, getApplicationContext());
                    recyclerView6.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(getApplicationContext(), OfferCourseActivity.class));
        }
    }

    /**public boolean onCreateOptionsMenu(Menu menu) {
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
                ArrayList<CourseOfferingsData> newList = new ArrayList<>();
                for (CourseOfferingsData data : list1) {
                    String query1 = data.getBatch().toLowerCase();
                    String query2 = data.getSemester().toLowerCase();
                    if (query1.contains(newText)) {
                        newList.add(data);
                    }
                    else if (query2.contains(newText)) {
                        newList.add(data);
                    }
                }
                adapter.setFilter(newList);
                recyclerView1.setAdapter(adapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/

}