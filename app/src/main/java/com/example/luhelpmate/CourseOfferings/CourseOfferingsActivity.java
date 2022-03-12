package com.example.luhelpmate.CourseOfferings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.luhelpmate.R;
import com.example.luhelpmate.Routine.RoutineViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class CourseOfferingsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseFirestore firestore;
    private TextView session, year, updatedate ;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_offerings);


        session = findViewById(R.id.session);
        year = findViewById(R.id.year);
        firestore = FirebaseFirestore.getInstance();
        DocumentReference df1 = firestore.collection("Session").document("Session");

        df1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    session.setText(value.getString("session"));
                    year.setText(value.getString("year"));
                }
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("1st Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("2nd Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("3rd Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("4th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("5th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("6th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("7th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("8th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("9th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("10th Semester"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        final CourseViewPagerAdapter adapter = new CourseViewPagerAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (value.getString("admin").equals("1")) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OfferCourse.class)));

    }
}