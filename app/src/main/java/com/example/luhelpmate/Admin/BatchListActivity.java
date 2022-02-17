package com.example.luhelpmate.Admin;

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

import com.example.luhelpmate.AdminAdapter.BatchListAdapter;
import com.example.luhelpmate.Data.BatchData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BatchListActivity extends AppCompatActivity {

    private EditText addBatch, addSection;
    private Button add;
    private ProgressBar progressBar;
    private RecyclerView addBatchRecyclerView;
    private ArrayList<BatchData> batchDataArrayList;
    private DatabaseReference reference, dbRef;
    private ProgressDialog pd;
    private String batch, section;

    BatchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);

        progressBar = findViewById(R.id.progressBar);

        pd = new ProgressDialog(this);

        TextView t= findViewById(R.id.t);
        t.setText("      ");

        addBatch = findViewById(R.id.enterBatch);
        addSection = findViewById(R.id.enterSection);
        add = findViewById(R.id.addB);

        addBatchRecyclerView = findViewById(R.id.addBatchRecycler);
        add.setOnClickListener(v -> checkValidation());
        adapter = new BatchListAdapter(batchDataArrayList, BatchListActivity.this);

        reference = FirebaseDatabase.getInstance().getReference().child("Batch List");
        Query query = reference.orderByChild("batch");
        query.addValueEventListener(new ValueEventListener() {
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
                    addBatchRecyclerView.setLayoutManager(new LinearLayoutManager(BatchListActivity.this));
                    adapter = new BatchListAdapter(batchDataArrayList, BatchListActivity.this);
                    addBatchRecyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BatchListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                for (BatchData data:batchDataArrayList){
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

    private void checkValidation() {
        batch = addBatch.getText().toString();
        section = addSection.getText().toString();

        if (batch.isEmpty()) {
            addBatch.setError("Enter Batch");
            addBatch.requestFocus();
        } else if(section.isEmpty()) {
            section="-";
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

        dbRef.child(batch).setValue(batchData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(BatchListActivity.this, "Added", Toast.LENGTH_SHORT).show();
            addBatch.setText("");
            addSection.setText("");

            Intent intent = new Intent(this, BatchListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Batch Notification")
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle("New Batch")
                    .setContentText("Batch: " +batch).setStyle(new NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context. NOTIFICATION_SERVICE ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel( "Batch Notification" , "Batch Notification" , importance) ;
                builder.setChannelId( "Batch Notification" ) ;
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel) ;
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                    builder.build()) ;

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(BatchListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }
}