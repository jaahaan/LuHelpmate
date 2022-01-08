package com.example.luhelpmate.User;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.luhelpmate.MainActivity;
import com.example.luhelpmate.R;
import com.example.luhelpmate.RoutineActivity;
import com.example.luhelpmate.SliderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    SliderView sliderView;
    int[] images = {R.drawable.lu1,
            R.drawable.adbanner, R.drawable.view1};

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        sliderView = findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.startAutoCycle();

        CardView aboutUs, courseOfferings, routine, books, faculty, cr, bus, notice;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aboutUsID) {
            Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(aboutIntent);
        }

        /**if (v.getId() == R.id.courseOffering) {
            Intent courseIntent = new Intent(HomeActivity.this, CourseOfferingsActivity.class);
            startActivity(courseIntent);
        }

        if (v.getId() == R.id.routine) {
            Intent routineIntent = new Intent(HomeActivity.this, RoutineActivity.class);
            startActivity(routineIntent);
        }

        if (v.getId() == R.id.books) {
            Intent booksIntent = new Intent(HomeActivity.this, BooksActivity.class);
            startActivity(booksIntent);
        }

        if (v.getId() == R.id.faculty) {
            Intent facultyIntent = new Intent(HomeActivity.this, UserFacultyActivity.class);
            startActivity(facultyIntent);
        }

        if (v.getId() == R.id.cr) {
            Intent crIntent = new Intent(HomeActivity.this, CRActivity.class);
            startActivity(crIntent);
        }

        if (v.getId() == R.id.bus) {
            Intent busIntent = new Intent(HomeActivity.this, TransportActivity.class);
            startActivity(busIntent);
        }
        if (v.getId() == R.id.notice) {
            Intent busIntent = new Intent(HomeActivity.this, UserNoticeActivity.class);
            startActivity(busIntent);
        }*/

    }

}