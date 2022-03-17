package com.example.luhelpmate.CourseOfferings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luhelpmate.Advisor.AdvisorList;
import com.example.luhelpmate.Book.BookAdapter;
import com.example.luhelpmate.Book.BookData;
import com.example.luhelpmate.R;
import com.example.luhelpmate.Routine.RoutineViewPagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CourseOfferingsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseFirestore firestore;
    private TextView session, year, updateDate ;
    private LinearLayout lastupdated;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_offerings);


        session = findViewById(R.id.session);
        year = findViewById(R.id.year);
        lastupdated = findViewById(R.id.lastUpdated);
        updateDate = findViewById(R.id.date);
        firestore = FirebaseFirestore.getInstance();

        Calendar forDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(forDate.getTime());

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

        DocumentReference df2 = firestore.collection("Last Updated").document("Course Offerings Updated");
        df2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    updateDate.setText(value.getString("date"));
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
        tabLayout.addTab(tabLayout.newTab().setText("11th Semester"));
        tabLayout.addTab(tabLayout.newTab().setText("12th Semester"));

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
                        lastupdated.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DocumentReference df = firestore.collection("Last Updated").document("Course Offerings Updated");

                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot value) {
                                        DocumentReference df = firestore.collection("Last Updated").document("Course Offerings Updated");
                                        Map<String, String> userInfo = new HashMap<>();
                                        userInfo.put("date", date);
                                        df.set(userInfo);
                                        Intent intent = new Intent(getApplicationContext(), CourseOfferingsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Advisor Notification")
                                                .setSmallIcon(R.drawable.splashicon)
                                                .setContentTitle("Course Offerings " + session.getText().toString() + year.getText().toString())
                                                .setStyle(new NotificationCompat.BigTextStyle())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setContentIntent(pendingIntent).setAutoCancel(true);

                                        NotificationManager mNotificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            int importance = NotificationManager.IMPORTANCE_HIGH;
                                            NotificationChannel notificationChannel = new NotificationChannel("Course Offerings Notification", "Course Offerings Notification", importance);
                                            builder.setChannelId("Course Offerings Notification");
                                            assert mNotificationManager != null;
                                            mNotificationManager.createNotificationChannel(notificationChannel);
                                        }
                                        assert mNotificationManager != null;
                                        mNotificationManager.notify((int) System.currentTimeMillis(),
                                                builder.build());
                                    }
                                });
                            }
                        });

                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        fab.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OfferCourse.class)));

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        MenuItem download = menu.findItem(R.id.download);

        download.setOnMenuItemClickListener(item -> {
            try{
                String fileText=retreaveDataFromServer();
                saveFile("test","test1",fileText,CourseOfferingsActivity.this);
                String savedString = "";
                savedString = readFile("test","test1",CourseOfferingsActivity.this);
                Log.v("stdntLst",savedString);
            }
            catch (Exception e){
                Log.e("stdntLst", e.toString());
            }


            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }
    private String retreaveDataFromServer(){
        String s="id, batch, section\n250, 50, F\n260, 53, E\n255, 50, C\n";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Course Offerings").child("10th");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {

                } else {

                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        for(DataSnapshot snapshot2: snapshot1.getChildren()){
                            if(stringCompare(snapshot2.getKey().toString(),"code")){

                            }
                            else if(stringCompare(snapshot2.getKey().toString(),"credit")){

                            }
                            else if(stringCompare(snapshot2.getKey().toString(),"initial")){

                            }
                            else if(stringCompare(snapshot2.getKey().toString(),"prerequisite")){

                            }
                            else if(stringCompare(snapshot2.getKey().toString(),"semester")){

                            }
                            else if(stringCompare(snapshot2.getKey().toString(),"title")){

                            }


                            Log.e("stdntLst2",snapshot2.getKey().toString()+"__"+snapshot2.getValue().toString());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return s;
    }
    private boolean stringCompare(String s, String s2){
        if(s.length()==s2.length()){
            int flg=0;
            for(int j=0;j<s.length();j++){
                if(s.charAt(j)!=s2.charAt(j)){
                    flg=1;
                    break;
                }
            }
            if(flg==0) return true;
        }
        return false;
    }
    private void saveFile(String folderName, String fileName, String fileText, Context context){
        FileOutputStream fileOutputStream = null;
        File file = null;
        fileName = fileName + ".csv";
        try{
            file = new File(context.getExternalFilesDir(folderName), fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileText.getBytes());

            Toast.makeText(CourseOfferingsActivity.this,"Saved to "+CourseOfferingsActivity.this.getExternalFilesDir("CourseAttendance")+"/"+fileName, Toast.LENGTH_LONG).show();
            ///openFile(file, currentCourse);
            ///shareFile(file, currentCourse);
            openFile(file,CourseOfferingsActivity.this);

        }
        catch (Exception e){
            Log.e("stdntLst", e.toString());
        }
        finally {
            if(fileOutputStream!=null){
                try{
                    fileOutputStream.close();
                }
                catch (Exception e){
                    Log.e("stdntLst", e.toString());
                }
            }
        }
    }
    private String readFile(String folderName, String fileName, Context context){
        String fileText = "";
        FileInputStream fileInputStream = null;
        File file = null;
        fileName = fileName + ".txt";
        try{
            file = new File(context.getExternalFilesDir(folderName), fileName);
            fileInputStream = new FileInputStream(file);
            int i = -1;
            StringBuffer buffer = new StringBuffer();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            fileText = buffer.toString();
            Log.v("StdntlstDM",fileText);
            //Toast.makeText(currentCourse.getContext(),"Saved to "+currentCourse.getContext().getExternalFilesDir("CourseAttendance")+"/"+fileName,Toast.LENGTH_LONG).show();
            ///openFile(file, currentCourse);
            ///shareFile(file, currentCourse);

        }
        catch (Exception e){
            Log.e("stdntLst", e.toString());
        }
        finally {
            if(fileInputStream!=null){
                try{
                    fileInputStream.close();
                }
                catch (Exception e){
                    Log.e("stdntLst", e.toString());
                }
            }
        }

        return fileText;

    }
    private void openFile(File file, Context context){
        try {

            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(context ,context.getPackageName() + ".provider", file);
            } else
            {
                uri = Uri.fromFile(file);
            }
            String mime="/*";
            ///Important_need2learnHowItWorks///
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            if(mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString())))
            {
                mime=mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
            }

            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri,mime);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);


        }
        catch (Exception e){
            Log.e("stdntLst", e.toString());
        }
    }

}