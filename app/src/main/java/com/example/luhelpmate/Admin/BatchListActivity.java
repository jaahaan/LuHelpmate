package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luhelpmate.Data.BatchData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class BatchListActivity extends AppCompatActivity {

    private EditText addBatch, addSection;
    private Button add;

    private RecyclerView addBatchRecyclerView;
    private LinkedList<BatchData> batchDataLinkedList;
    private DatabaseReference reference, dbRef;
    private ProgressDialog pd;
    private String batch, section;

    BatchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        reference = FirebaseDatabase.getInstance().getReference().child("Batch List");

        pd = new ProgressDialog(this);

        addBatch = findViewById(R.id.enterBatch);
        addSection = findViewById(R.id.enterSection);
        add = findViewById(R.id.addB);

        addBatchRecyclerView = findViewById(R.id.addBatchRecycler);
        add.setOnClickListener(v -> checkValidation());

        batchSection();

    }

    private void batchSection() {
        dbRef = reference;
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                batchDataLinkedList = new LinkedList<>();

                if (!snapshot.exists()) {
                    addBatchRecyclerView.setVisibility(View.GONE);
                } else {
                    addBatchRecyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        BatchData data = snapshot1.getValue(BatchData.class);
                        batchDataLinkedList.add(data);
                    }
                    addBatchRecyclerView.setHasFixedSize(true);
                    addBatchRecyclerView.setLayoutManager(new LinearLayoutManager(BatchListActivity.this));
                    adapter = new BatchListAdapter(batchDataLinkedList, BatchListActivity.this);
                    addBatchRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BatchListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {
        batch = addBatch.getText().toString();
        section = addSection.getText().toString();

        if (batch.isEmpty()) {
            addBatch.setError("Enter Batch");
            addBatch.requestFocus();
        } else if(section.isEmpty()) {
            section="-";
            addSection.setText("-");
            pd.setMessage("Adding...");
            pd.show();
            uploadData();
        }
        else {
            pd.setMessage("Adding...");
            pd.show();
            uploadData();
        }
    }

    private void uploadData() {
        dbRef = reference;
        final String uniqueKey = dbRef.push().getKey();

        BatchData batchData = new BatchData(batch, section, uniqueKey);

        dbRef.child(uniqueKey).setValue(batchData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(BatchListActivity.this, "Added", Toast.LENGTH_SHORT).show();
            addBatch.setText("");
            addSection.setText("");
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(BatchListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }
}