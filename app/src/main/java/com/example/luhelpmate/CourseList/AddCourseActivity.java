package com.example.luhelpmate.CourseList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddCourseActivity extends AppCompatActivity {

    private AutoCompleteTextView cCode, cTitle, cCredit, cPrerequisite;
    private Button add;
    private DatabaseReference reference, dbRef, cRef;
    private String code, title, credit, prerequisite;
    Pattern n = Pattern.compile("\\D");

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");
        cRef = FirebaseDatabase.getInstance().getReference().child("Course List");
        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchCourse(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        pd = new ProgressDialog(this);

        cCode = findViewById(R.id.codeC);
        cTitle = findViewById(R.id.titleC);
        cCredit = findViewById(R.id.creditC);
        cPrerequisite = findViewById(R.id.prerequisiteC);
        add = findViewById(R.id.addC);

        add.setOnClickListener(v -> checkValidation());

    }
    private void searchCourse(DataSnapshot snapshot) {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("code").getValue(String.class);
                String courseTitle = dataSnapshot.child("title").getValue(String.class);
                list1.add(courseCode);
                list2.add(courseTitle);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list1);
            ArrayAdapter titleAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list2);
            cCode.setAdapter(codeAdapter);
            cCode.setThreshold(1);
            cPrerequisite.setAdapter(codeAdapter);
            cPrerequisite.setThreshold(1);
            cTitle.setAdapter(titleAdapter);
            cTitle.setThreshold(1);
        }
    }

    private void checkValidation() {
        code = cCode.getText().toString();
        title = cTitle.getText().toString();
        credit = cCredit.getText().toString();
        prerequisite = cPrerequisite.getText().toString();
        if (code.isEmpty()) {
            cCode.setError("Enter Course Code");
            cCode.requestFocus();
        } else if (title.isEmpty()) {
            cTitle.setError("Enter Course Title");
            cTitle.requestFocus();
        } else if (n.matcher(title).matches()) {
            cTitle.setError("Enter Valid Course Title");
            cTitle.requestFocus();
        } else if (credit.isEmpty()) {
            cCredit.setError("Enter Course Credit");
            cCredit.requestFocus();
        } else {
            if (prerequisite.isEmpty()) {
                prerequisite ="-";
            }
            pd.setMessage("Adding...");
            pd.show();
            uploadData();
        }
    }

    private void uploadData() {
        dbRef = reference;
        final String uniqueKey = dbRef.push().getKey();

        CourseData courseOfferData = new CourseData(code, title, credit, prerequisite, uniqueKey);

        dbRef.child(uniqueKey).setValue(courseOfferData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, CourseList.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Course Notification")
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle("New Course")
                    .setContentText("Course Title: " +title).setStyle(new NotificationCompat.BigTextStyle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context. NOTIFICATION_SERVICE ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel( "Course Notification" , "Course Notification" , importance) ;
                builder.setChannelId( "Course Notification" ) ;
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel) ;
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                    builder.build()) ;

        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddCourseActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }
}