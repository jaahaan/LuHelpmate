package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.AdminAdapter.FacultyAdapter;
import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class FacultyActivityAdmin extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<FacultyData> list;
    private DatabaseReference reference;
    FacultyAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        TextView header = findViewById(R.id.header);
        header.setText("Faculty Members");
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);


        reference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");


        Query query = reference.orderByChild("no");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                if (!snapshot.exists()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        FacultyData data = snapshot1.getValue(FacultyData.class);
                        list.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FacultyActivityAdmin.this));
                    adapter = new FacultyAdapter(list, FacultyActivityAdmin.this);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()){
                    if (value.getString("admin").equals("1")){
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fab.setOnClickListener(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<FacultyData> newList = new ArrayList<>();

                for (FacultyData data : list) {
                    String query1 = data.getName().toLowerCase();
                    String query2 = data.getDesignation().toLowerCase();
                    String query3 = data.getInitial().toLowerCase();
                    if (query1.contains(newText)) {
                        newList.add(data);
                    }
                    else if (query2.contains(newText)) {
                        newList.add(data);
                    }
                    else if (query3.contains(newText)) {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            startActivity(new Intent(FacultyActivityAdmin.this, AddFacultyActivity.class));
        }
    }
}