package com.example.luhelpmate.Advisor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Batch.BatchListActivity;
import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdvisorList extends AppCompatActivity {

    FloatingActionButton fab;
    private TextView updateDate, textView;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private ArrayList<AdvisorData> list;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    AdvisorAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        TextView header = findViewById(R.id.header);
        header.setText("Batch-wise Advisors");
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        updateDate = findViewById(R.id.date);
        linearLayout = findViewById(R.id.linear);
        linearLayout.setVisibility(View.VISIBLE);
        firestore = FirebaseFirestore.getInstance();
        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(forDate.getTime());

        DocumentReference df1 = firestore.collection("Last Updated").document("Advisor Updated");

        df1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    updateDate.setText(value.getString("date"));
                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Advisor List");
        Query query = reference.orderByChild("batch");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                if (!snapshot.exists()) {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AdvisorData data = snapshot1.getValue(AdvisorData.class);
                        list.add(data);
                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new AdvisorAdapter(list, getApplicationContext());
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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (value.getString("admin").equals("1")) {
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DocumentReference df = firestore.collection("Last Updated").document("Advisor Updated");

                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot value) {
                                        DocumentReference df = firestore.collection("Last Updated").document("Advisor Updated");
                                        Map<String, String> userInfo = new HashMap<>();
                                        userInfo.put("date", date);
                                        df.set(userInfo);
                                        Intent intent = new Intent(getApplicationContext(), AdvisorList.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Advisor Notification")
                                                .setSmallIcon(R.drawable.splashicon)
                                                .setContentTitle("Updated Advisor List")
                                                .setStyle(new NotificationCompat.BigTextStyle())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setContentIntent(pendingIntent).setAutoCancel(true);

                                        NotificationManager mNotificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationChannel notificationChannel = new NotificationChannel("Advisor Notification", "Advisor Notification", importance);
                                            builder.setChannelId("Advisor Notification");
                                            assert mNotificationManager != null;
                                            mNotificationManager.createNotificationChannel(notificationChannel);
                                        }
                                        assert mNotificationManager != null;
                                        mNotificationManager.notify((int) System.currentTimeMillis(),
                                                builder.build());
                                    }
                                });
                            }
                        });

                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddAdvisor.class)));

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
                ArrayList<AdvisorData> newList = new ArrayList<>();
                for (AdvisorData data : list) {
                    String query1 = data.getBatch().toLowerCase();
                    String query2 = data.getSection().toLowerCase();
                    String query3 = data.getName1().toLowerCase();
                    String query4 = data.getName2().toLowerCase();
                    if (query1.contains(newText)) {
                        newList.add(data);
                    } else if (query2.contains(newText)) {
                        newList.add(data);
                    } else if (query3.contains(newText)) {
                        newList.add(data);
                    } else if (query4.contains(newText)) {
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