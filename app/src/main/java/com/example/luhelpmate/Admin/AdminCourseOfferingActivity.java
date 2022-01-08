package com.example.luhelpmate.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminCourseOfferingActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    private RecyclerView courseOfferingRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_offering);

        courseOfferingRecycler = findViewById(R.id.courseOfferingRecycler);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fab){
            startActivity(new Intent(AdminCourseOfferingActivity.this, OfferCourseActivity.class));
        }
    }
}