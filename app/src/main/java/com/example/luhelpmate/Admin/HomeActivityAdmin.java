package com.example.luhelpmate.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.luhelpmate.AdminAdapter.ClubAdapter;
import com.example.luhelpmate.Data.ClubsData;
import com.example.luhelpmate.R;
import com.example.luhelpmate.RoutineActivity;
import com.example.luhelpmate.Settings;
import com.example.luhelpmate.SliderAdapter;
import com.example.luhelpmate.User.BookActivityUser;
import com.example.luhelpmate.User.CourseListUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class HomeActivityAdmin extends AppCompatActivity {

    SliderView sliderView;
    int[] images = {R.drawable.lu1, R.drawable.view1,
            R.drawable.view2, R.drawable.view3, R.drawable.view4, R.drawable.view5,
            R.drawable.view6, R.drawable.view7};

    private FirebaseAuth mAuth;
    private TextView textView, date;
    private CardView courseOfferings, routine, books, faculty, cr, notice, batch, courseList;
    ImageView fb, website, map;
    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;
    private ClubAdapter adapter;
    private ArrayList<ClubsData> list;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        recyclerView = findViewById(R.id.recycler);

        mAuth = FirebaseAuth.getInstance();

        sliderView = findViewById(R.id.image_slider);
        textView = findViewById(R.id.text);
        date = findViewById(R.id.date);

        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yy");
        String year = currentDate.format(forDate.getTime());
        firestore =FirebaseFirestore.getInstance();
        DocumentReference df1 = firestore.collection("Session").document("Session");

        df1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()) {
                    textView.setText(value.getString("session"));
                    date.setText(value.getString("year"));
                }
            }
        });

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.startAutoCycle();

        courseOfferings = findViewById(R.id.courseOffering);
        routine = findViewById(R.id.routine);
        books = findViewById(R.id.books);
        faculty = findViewById(R.id.faculty);
        cr = findViewById(R.id.cr);
        batch = findViewById(R.id.batch);
        notice = findViewById(R.id.notice);
        courseList = findViewById(R.id.courseList);
        fb = findViewById(R.id.fb);
        website = findViewById(R.id.website);
        map = findViewById(R.id.map);


        /**courseOfferings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseOfferingActivityAdmin.class)));
        routine.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RoutineActivityAdmin.class)));
        books.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BookActivityAdmin.class)));
        faculty.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FacultyActivityAdmin.class)));
        cr.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CrActivityAdmin.class)));
        batch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BatchListActivity.class)));
        courseList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseList.class)));
        notice.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NoticeActivityAdmin.class)));
*/

        fb.setOnClickListener(v -> gotoUrl("https://www.facebook.com/groups/officiallucse"));

        website.setOnClickListener(v -> gotoUrl("https://www.lus.ac.bd/"));
        map.setOnClickListener(v -> openMap());


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()) {
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
                        courseOfferings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseOfferingActivityAdmin.class)));
                        routine.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RoutineActivity.class)));
                        books.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BookActivityAdmin.class)));
                        faculty.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FacultyActivityAdmin.class)));
                        cr.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CrActivityAdmin.class)));
                        batch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PreviousQuestionsActivity.class)));
                        courseList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseList.class)));
                        notice.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NoticeActivityAdmin.class)));

                    } else {
                        courseOfferings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseOfferingActivityAdmin.class)));
                        routine.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RoutineActivity.class)));
                        books.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), BookActivityUser.class)));
                        faculty.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FacultyActivityAdmin.class)));
                        cr.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CrActivityAdmin.class)));
                        batch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PreviousQuestionsActivity.class)));
                        courseList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CourseListUser.class)));
                        notice.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NoticeActivityAdmin.class)));

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


}