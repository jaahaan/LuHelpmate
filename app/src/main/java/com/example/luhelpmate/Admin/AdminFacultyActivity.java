package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminFacultyActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    private ListView listView;
    private ArrayList<FacultyData> list;
    private DatabaseReference reference;
    FacultyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faculty);

        listView = findViewById(R.id.facultyListView);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");

        faculty();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    private void faculty() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if(!snapshot.exists()){
                    listView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        FacultyData data = snapshot1.getValue(FacultyData.class);
                        list.add(data);
                    }
                    adapter = new FacultyAdapter(AdminFacultyActivity.this, list);
                    listView.setAdapter(adapter);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str){


    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(AdminFacultyActivity.this, AddFacultyActivity.class));
        }
    }
}