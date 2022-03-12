package com.example.luhelpmate.TimeSlot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Batch.BatchData;
import com.example.luhelpmate.Batch.BatchListActivity;
import com.example.luhelpmate.Batch.BatchListAdapter;
import com.example.luhelpmate.Book.BookAdapter;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TimeSlotActivity extends AppCompatActivity {

    private EditText addTime;
    private Button add;
    private ProgressBar progressBar;
    private TextView textView;
    private RecyclerView recyclerView;
    private ArrayList<TimeSlotData> list;
    private DatabaseReference reference, dbRef;
    private ProgressDialog pd;
    private String time;
    TimeSlotAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        pd = new ProgressDialog(this);

        addTime = findViewById(R.id.time);
        add = findViewById(R.id.add);

        recyclerView = findViewById(R.id.recycler);
        add.setOnClickListener(v -> checkValidation());
        adapter = new TimeSlotAdapter(list, getApplicationContext());

        reference = FirebaseDatabase.getInstance().getReference().child("Time Slot");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        TimeSlotData data = snapshot1.getValue(TimeSlotData.class);
                        list.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new TimeSlotAdapter(list, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    textView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkValidation() {
        time = addTime.getText().toString();
        if (time.isEmpty()) {
            addTime.setError("Enter Batch");
            addTime.requestFocus();
        } else {
            pd.setMessage("Adding...");
            pd.show();
            uploadData();
        }
    }

    private void uploadData() {
        dbRef = reference;
        final String uniqueKey = dbRef.push().getKey();

        TimeSlotData data = new TimeSlotData(time, uniqueKey);

        dbRef.child(time).setValue(data).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
            addTime.setText("");

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                ArrayList<TimeSlotData> newList = new ArrayList<>();
                for (TimeSlotData data : list) {
                    String query1 = data.getTime().toLowerCase();
                    if (query1.contains(newText)) {
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