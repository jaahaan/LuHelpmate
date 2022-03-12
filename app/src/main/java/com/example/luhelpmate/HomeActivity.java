package com.example.luhelpmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Advisor.AdvisorList;
import com.example.luhelpmate.CourseOfferings.CourseOfferingsActivity;
import com.example.luhelpmate.Menu.Menu;
import com.example.luhelpmate.Book.BookActivityAdmin;
import com.example.luhelpmate.CourseList.CourseList;
import com.example.luhelpmate.CR.CrActivityAdmin;
import com.example.luhelpmate.Faculty.FacultyActivity;
import com.example.luhelpmate.Notice.NoticeActivityAdmin;
import com.example.luhelpmate.Routine.RoutineActivity;
import com.example.luhelpmate.Slider.SliderAdapter;

import com.example.luhelpmate.Slider.SliderData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SliderView sliderView;
    private ArrayList<SliderData> list;
    private DatabaseReference reference;
    SliderAdapter adapter;

    private FirebaseAuth mAuth;
    private TextView dept, textView, date;
    private CardView courseOfferings, routine, books, faculty, cr, notice, advisor, courseList;
    ImageView fb, website, map;
    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        sliderView = findViewById(R.id.image_slider);
        dept = findViewById(R.id.dept);
        textView = findViewById(R.id.text);
        date = findViewById(R.id.date);

        Calendar forYear = Calendar.getInstance();
        SimpleDateFormat currentYear = new SimpleDateFormat("yy");
        String year = currentYear.format(forYear.getTime());
        firestore = FirebaseFirestore.getInstance();
        DocumentReference df1 = firestore.collection("Session").document("Session");

        df1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    textView.setText(value.getString("session"));
                    date.setText(value.getString("year"));
                }
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Slider Image");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    sliderView.setVisibility(View.GONE);
                } else {
                    sliderView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        SliderData data = snapshot1.getValue(SliderData.class);
                        list.add(data);
                    }
                    adapter = new SliderAdapter(list, getApplicationContext());
                    sliderView.setSliderAdapter(adapter);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL);
                    sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                    sliderView.startAutoCycle();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        courseOfferings = findViewById(R.id.courseOffering);
        routine = findViewById(R.id.routine);
        books = findViewById(R.id.books);
        faculty = findViewById(R.id.faculty);
        cr = findViewById(R.id.cr);
        advisor = findViewById(R.id.advisor);
        notice = findViewById(R.id.notice);
        courseList = findViewById(R.id.courseList);
        fb = findViewById(R.id.fb);
        website = findViewById(R.id.website);
        map = findViewById(R.id.map);

        fb.setOnClickListener(v -> gotoUrl("https://www.facebook.com/groups/officiallucse"));

        website.setOnClickListener(v -> gotoUrl("https://www.lus.ac.bd/"));
        map.setOnClickListener(v -> openMap());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (!value.getString("admin").equals("") && !value.getString("department").equals("")) {
                        if (value.getString("admin").equals("1")) {
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DocumentReference df = firestore.collection("Session").document("Session");

                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot value) {
                                            DocumentReference df = firestore.collection("Session").document("Session");
                                            if (value.getString("session").equals("Summer-")) {
                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("session", "Spring-");
                                                userInfo.put("year", year);
                                                df.set(userInfo);
                                            } else if (value.getString("session").equals("Spring-")) {
                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("session", "Summer-");
                                                userInfo.put("year", year);
                                                df.set(userInfo);
                                            }
                                        }
                                    });
                                }
                            });

                        }
                        courseOfferings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseOfferingsActivity.class)));
                        routine.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RoutineActivity.class)));
                        cr.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CrActivityAdmin.class)));
                        faculty.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FacultyActivity.class)));
                        notice.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NoticeActivityAdmin.class)));
                        books.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BookActivityAdmin.class)));
                        advisor.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AdvisorList.class)));
                        courseList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseList.class)));

                        if (value.getString("department").equals("CSE"))
                            dept.setText("Department of CSE");
                        else if (value.getString("department").equals("EEE"))
                            dept.setText("Department of EEE");
                        else if (value.getString("department").equals("ARCHI"))
                            dept.setText("Department of ARCHI");
                        else if (value.getString("department").equals("CE"))
                            dept.setText("Department of CE");
                        else if (value.getString("department").equals("ENG"))
                            dept.setText("Department of ENG");
                        else if (value.getString("department").equals("LAW"))
                            dept.setText("Department of LAW");
                        else if (value.getString("department").equals("IS"))
                            dept.setText("Department of IS");
                        else if (value.getString("department").equals("BNG"))
                            dept.setText("Department of BNG");
                        else if (value.getString("department").equals("THM"))
                            dept.setText("Department of THM");
                        else if (value.getString("department").equals("PH"))
                            dept.setText("Department of PH");

                    } else {
                        courseOfferings.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        routine.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        cr.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        faculty.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        notice.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        books.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        advisor.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                        courseList.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Sign Out and provide info", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });


    }


    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void openMap() {
        Uri uri = Uri.parse("geo:0, 0?q=Leading University, Sylhet.");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        MenuItem menuItem = menu.findItem(R.id.profile);
        View view = MenuItemCompat.getActionView(menuItem);
        CircleImageView profileImage = view.findViewById(R.id.toolbar_profileImage);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Uri image = account.getPhotoUrl();
            Glide.with(this).load(image).circleCrop().into(profileImage);
        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Menu.class));
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}