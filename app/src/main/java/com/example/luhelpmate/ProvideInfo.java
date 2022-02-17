package com.example.luhelpmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProvideInfo extends AppCompatActivity {


    TabLayout tabLayout;
    ImageView image;
    ViewPager viewPager;
    float v=0;
    private FirebaseAuth mAuth;
    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_info);

        mAuth = FirebaseAuth.getInstance();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        image = findViewById(R.id.image);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Uri uri = account.getPhotoUrl();
        Glide.with(this).load(uri).circleCrop().into(image);

        tabLayout.addTab(tabLayout.newTab().setText("Teacher"));
        tabLayout.addTab(tabLayout.newTab().setText("Student"));
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
        final  ProfileAdapter adapter = new ProfileAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.setTranslationY(300);

        //tabLayout.setAlpha(v);

        //tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();


    }
}