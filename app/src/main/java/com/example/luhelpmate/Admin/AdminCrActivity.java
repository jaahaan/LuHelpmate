package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luhelpmate.Data.CrData;

import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCrActivity extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton fab1;

    private ListView crListView;
    private ArrayList<CrData> list;
    private DatabaseReference reference;

    CrAdapter crAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cr);

        crListView = findViewById(R.id.crList);

        // Get a reference to our posts
        reference = FirebaseDatabase.getInstance().getReference().child("Cr Info");

        cr();

        fab1 = findViewById(R.id.fabC);
        fab1.setOnClickListener(this);

    }
    private void cr() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if(!snapshot.exists()){
                    crListView.setVisibility(View.GONE);
                } else {
                    crListView.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        CrData data = snapshot1.getValue(CrData.class);
                        list.add(data);
                    }
                    crAdapter = new CrAdapter(AdminCrActivity.this, list);
                    crListView.setAdapter(crAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminCrActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabC) {
            startActivity(new Intent(AdminCrActivity.this, AddCrActivity.class));
        }
    }
}