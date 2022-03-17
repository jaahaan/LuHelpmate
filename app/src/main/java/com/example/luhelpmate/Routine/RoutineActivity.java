package com.example.luhelpmate.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.luhelpmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RoutineActivity extends AppCompatActivity{

    private FloatingActionButton fab;
    private FirebaseFirestore firestore;
    private TextView session, year ;
    private RoutineViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

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

        tabLayout.addTab(tabLayout.newTab().setText("SUN"));
        tabLayout.addTab(tabLayout.newTab().setText("MON"));
        tabLayout.addTab(tabLayout.newTab().setText("TUE"));
        tabLayout.addTab(tabLayout.newTab().setText("WED"));
        tabLayout.addTab(tabLayout.newTab().setText("THU"));
        tabLayout.addTab(tabLayout.newTab().setText("FRI"));
        tabLayout.addTab(tabLayout.newTab().setText("SAT"));
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

        adapter = new RoutineViewPagerAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()){
                    if (value.getString("admin").equals("1")){
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        Pattern digit = Pattern.compile("[\\d]+");
                        Matcher matcherDigit = digit.matcher(value.getString("initial"));

                        //For Alphabet
                        Pattern letter = Pattern.compile("[A-Z]+");
                        Matcher matcherLetter = letter.matcher(value.getString("initial"));
                        if (matcherDigit.find()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cr Info");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String emailData = dataSnapshot.child("email").getValue(String.class);
                                            String batchData = dataSnapshot.child("batch").getValue(String.class);
                                            if (value.getString("email").equals(emailData)) {
                                                fab.setVisibility(View.VISIBLE);
                                            } else {
                                                fab.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            String data = dataSnapshot.child("email").getValue(String.class);
                                            if (value.getString("email").equals(data)) {
                                                fab.setVisibility(View.VISIBLE);
                                            } else {
                                                fab.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Make_Routine.class));
            }
        });

    }

}