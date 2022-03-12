package com.example.luhelpmate.Batch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchListUser extends AppCompatActivity {
    private EditText addBatch, addSection;
    private Button add;
    private ProgressBar progressBar;
    private RecyclerView addBatchRecyclerView;
    private ArrayList<BatchData> batchDataArrayList;
    private DatabaseReference reference;
    private ProgressDialog pd;
    private LinearLayout layout;

    BatchListAdapterUser adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        progressBar = findViewById(R.id.progressBar);

        pd = new ProgressDialog(this);

        addBatch = findViewById(R.id.enterBatch);
        addSection = findViewById(R.id.enterSection);
        addBatch.setVisibility(View.GONE);
        addSection.setVisibility(View.GONE);
        add = findViewById(R.id.addB);
        layout = findViewById(R.id.linearlayout);
        layout.setVisibility(View.GONE);
        add.setVisibility(View.GONE);


        addBatchRecyclerView = findViewById(R.id.addBatchRecycler);

        reference = FirebaseDatabase.getInstance().getReference().child("Batch List");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                batchDataArrayList = new ArrayList<>();

                if (!snapshot.exists()) {
                    addBatchRecyclerView.setVisibility(View.GONE);
                } else {
                    addBatchRecyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        BatchData data = snapshot1.getValue(BatchData.class);
                        batchDataArrayList.add(data);
                    }
                    addBatchRecyclerView.setHasFixedSize(true);
                    addBatchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new BatchListAdapterUser(batchDataArrayList, getApplicationContext());
                    addBatchRecyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                ArrayList<BatchData> newList = new ArrayList<>();
                for (BatchData data : batchDataArrayList) {
                    String query1 = data.getBatch().toLowerCase();
                    String query2 = data.getSection().toLowerCase();
                    if (query1.contains(newText)) {
                        newList.add(data);
                    }
                    else if (query2.contains(newText)) {
                        newList.add(data);
                    }
                }
                adapter.setFilter(newList);
                addBatchRecyclerView.setAdapter(adapter);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}