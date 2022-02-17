package com.example.luhelpmate.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.MainActivity;
import com.example.luhelpmate.R;
import com.example.luhelpmate.RoutineActivity;
import com.example.luhelpmate.Settings;
import com.example.luhelpmate.SliderAdapter;
import com.example.luhelpmate.UserAdapter.BatchListAdapterUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivityUser extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    SliderView sliderView;
    int[] images = {R.drawable.lu1,
            R.drawable.adbanner, R.drawable.view1};

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

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
        bus = findViewById(R.id.batch);
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

        MenuItem menuItem = menu.findItem(R.id.profile);
        View view = MenuItemCompat.getActionView(menuItem);
        CircleImageView profileImage = view.findViewById(R.id.toolbar_profileImage);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            Uri image = account.getPhotoUrl();
            Glide.with(this).load(image).circleCrop().into(profileImage);

        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.signOut){
            mAuth.signOut();
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aboutUsID) {
            Intent aboutIntent = new Intent(HomeActivityUser.this, AboutActivity.class);
            startActivity(aboutIntent);
        }
        if (v.getId() == R.id.faculty) {
            Intent facultyIntent = new Intent(getApplicationContext(), FacultyActivityUser.class);
            startActivity(facultyIntent);
        }

        if (v.getId() == R.id.cr) {
            Intent crIntent = new Intent(getApplicationContext(), CrActivityUser.class);
            startActivity(crIntent);
        }

        if (v.getId() == R.id.batch) {
            Intent busIntent = new Intent(getApplicationContext(), BatchListUser.class);
            startActivity(busIntent);
        }

        if (v.getId() == R.id.books) {
            Intent booksIntent = new Intent(getApplicationContext(), CourseListUser.class);
            startActivity(booksIntent);
        }

        if (v.getId() == R.id.notice) {
            Intent busIntent = new Intent(getApplicationContext(), NoticeActivityUser.class);
            startActivity(busIntent);
        }

        if (v.getId() == R.id.routine) {
            Intent routineIntent = new Intent(getApplicationContext(), BookActivityUser.class);
            startActivity(routineIntent);
        }

        /**if (v.getId() == R.id.courseOffering) {
            Intent courseIntent = new Intent(HomeActivity.this, CourseOfferingsActivity.class);
            startActivity(courseIntent);
        }

        if (v.getId() == R.id.routine) {
            Intent routineIntent = new Intent(HomeActivity.this, RoutineActivity.class);
            startActivity(routineIntent);
        }
         */

    }

}