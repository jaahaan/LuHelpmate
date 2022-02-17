package com.example.luhelpmate.User;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.UserAdapter.NoticeAdapterUser;
import com.example.luhelpmate.Data.NoticeData;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoticeActivityUser extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ArrayList<NoticeData> list;
    private NoticeAdapterUser adapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        TextView header = findViewById(R.id.header);
        header.setText("Notice Board");
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notice");
        Query query = reference.orderByChild("date");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                if (!snapshot.exists()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        NoticeData data = snapshot1.getValue(NoticeData.class);
                        list.add(0,data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new NoticeAdapterUser(list, getApplicationContext());
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
                ArrayList<NoticeData> newList = new ArrayList<>();
                for (NoticeData data : list) {
                    String query1 = data.getDate().toLowerCase();
                    String query2 = data.getTitle().toLowerCase();
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