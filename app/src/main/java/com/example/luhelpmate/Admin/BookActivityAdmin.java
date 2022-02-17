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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.luhelpmate.AdminAdapter.BookAdapter;
import com.example.luhelpmate.Data.BookData;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookActivityAdmin extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArrayList<BookData> list;
    private DatabaseReference reference;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Books");
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
                        BookData data = snapshot1.getValue(BookData.class);
                        list.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new BookAdapter(list, getApplicationContext());
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBookActivityAdmin.class));
            }
        });
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
                ArrayList<BookData> newList = new ArrayList<>();
                for (BookData data : list) {
                    String name1 = data.getBookName().toLowerCase();
                    String name2 = data.getCode().toLowerCase();
                    String name3 = data.getAuthor().toLowerCase();
                    if (name1.contains(newText)) {
                        newList.add(data);
                    }
                    else if (name2.contains(newText)) {
                        newList.add(data);
                    }
                    else if (name3.contains(newText)) {
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