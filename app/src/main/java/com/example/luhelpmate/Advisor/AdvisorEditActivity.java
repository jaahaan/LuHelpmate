package com.example.luhelpmate.Advisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AdvisorEditActivity extends AppCompatActivity {

    private AutoCompleteTextView Abatch, Asection, Aname1, Aname2;
    private Button update;
    private DatabaseReference reference, dbRef, bRef, tRef;
    private String batch, section, name1, name2;
    Pattern n = Pattern.compile("[a-z A-Z.]*");

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit);

        pd = new ProgressDialog(this);

        tRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        bRef = FirebaseDatabase.getInstance().getReference().child("Batch List");

        reference = FirebaseDatabase.getInstance().getReference().child("Advisor List");

        batch = getIntent().getStringExtra("batch");
        section = getIntent().getStringExtra("section");
        name1 = getIntent().getStringExtra("name1");
        name2 = getIntent().getStringExtra("name2");

        Abatch = findViewById(R.id.advisor);
        Asection = findViewById(R.id.section);
        Aname1 = findViewById(R.id.name1);
        Aname2 = findViewById(R.id.name2);
        update = findViewById(R.id.update);

        Abatch.setText(batch);
        Asection.setText(section);
        Aname1.setText(name1);
        Aname2.setText(name2);

        tRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchTeacher(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchBatch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(v -> checkValidation());
    }

    private void searchTeacher(DataSnapshot snapshot) {
        ArrayList<String> teachers = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String teacherData = dataSnapshot.child("name").getValue(String.class);
                teachers.add(teacherData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, teachers);
            Aname1.setAdapter(arrayAdapter);
            Aname1.setThreshold(1);

            Aname2.setAdapter(arrayAdapter);
            Aname2.setThreshold(1);
        }
    }
    private void searchBatch(DataSnapshot snapshot) {
        ArrayList<String> batchs = new ArrayList<>();
        ArrayList<String> section = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String batchData = dataSnapshot.child("batch").getValue(String.class);
                String sectionData = dataSnapshot.child("section").getValue(String.class);
                batchs.add(batchData);
                section.add(sectionData);
            }
            ArrayAdapter batchAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, batchs);
            ArrayAdapter sectionAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, section);
            Abatch.setAdapter(batchAdapter);
            Abatch.setThreshold(1);
            Asection.setAdapter(sectionAdapter);
            Asection.setThreshold(1);
        }
    }

    private void checkValidation() {
        batch = Abatch.getText().toString().trim();
        section = Asection.getText().toString().trim();
        name1 = Aname1.getText().toString().trim();
        name2 = Aname2.getText().toString().trim();

        if (batch.isEmpty()) {
            Abatch.setError("Empty");
            Abatch.requestFocus();
        } else if (section.isEmpty()) {
            Asection.setError("Empty");
            Aname1.requestFocus();
        } else if (name1.isEmpty()) {
            Aname1.setError("Empty");
            Aname1.requestFocus();
        } else if (!n.matcher(name1).matches()) {
            Aname1.setError("Name can be only Alphabet");
            Aname1.requestFocus();
        } else if (!n.matcher(name2).matches()) {
            Aname2.setError("Name can be only Alphabet");
            Aname2.requestFocus();
        } else {
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
        }
        }

    private void uploadData() {
        String uniqueKey = getIntent().getStringExtra("key");

        HashMap<String, Object> map = new HashMap<>();
        map.put("batch", batch);
        map.put("section", section);
        map.put("name1", name1);
        map.put("name2", name2);
        map.put("key", uniqueKey);
        reference.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, AdvisorList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

}