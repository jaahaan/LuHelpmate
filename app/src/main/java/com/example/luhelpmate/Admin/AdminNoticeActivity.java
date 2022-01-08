package com.example.luhelpmate.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminNoticeActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(AdminNoticeActivity.this, UpdateNoticeActivity.class));
        }
    }
}