package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView aboutUs, courseOfferings, routine, books, faculty, cr, bus, notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aboutUs = findViewById(R.id.aboutUsID);
        courseOfferings = findViewById(R.id.courseOffering);
        routine = findViewById(R.id.routine);
        books = findViewById(R.id.books);
        faculty = findViewById(R.id.faculty);
        cr = findViewById(R.id.cr);
        bus = findViewById(R.id.bus);
        notice = findViewById(R.id.notice);

        aboutUs.setOnClickListener(this);
        courseOfferings.setOnClickListener(this);
        routine.setOnClickListener(this);
        books.setOnClickListener(this);
        faculty.setOnClickListener(this);
        cr.setOnClickListener(this);
        bus.setOnClickListener(this);
        notice.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.aboutUsID){
            Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        }

        if (v.getId()==R.id.courseOffering){
            Intent courseIntent = new Intent(HomeActivity.this, CourseOfferingsActivity.class);
            startActivity(courseIntent);
        }

        if (v.getId()==R.id.routine){
            Intent routineIntent = new Intent(HomeActivity.this, RoutineActivity.class);
            startActivity(routineIntent);
        }

        if (v.getId()==R.id.books){
            Intent booksIntent = new Intent(HomeActivity.this, BooksActivity.class);
            startActivity(booksIntent);
        }

        if (v.getId()==R.id.faculty){
            Intent facultyIntent = new Intent(HomeActivity.this, FacultyActivity.class);
            startActivity(facultyIntent);
        }

        if (v.getId()==R.id.cr){
            Intent crIntent = new Intent(HomeActivity.this, CRActivity.class);
            startActivity(crIntent);
        }

        if (v.getId()==R.id.bus){
            Intent busIntent = new Intent(HomeActivity.this, TransportActivity.class);
            startActivity(busIntent);
        }
        if (v.getId()==R.id.notice){
            Intent busIntent = new Intent(HomeActivity.this, NoticeActivity.class);
            startActivity(busIntent);
        }

    }
}