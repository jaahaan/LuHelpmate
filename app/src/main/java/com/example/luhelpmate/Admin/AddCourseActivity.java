package com.example.luhelpmate.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {

    private EditText cCode, cTitle, cCredit, cPrerequisite;
    private Button add;
    private DatabaseReference reference, dbRef;
    private String code, title, credit, prerequisite;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        reference = FirebaseDatabase.getInstance().getReference().child("Course List");

        pd = new ProgressDialog(this);

        cCode = findViewById(R.id.codeC);
        cTitle = findViewById(R.id.titleC);
        cCredit = findViewById(R.id.creditC);
        cPrerequisite = findViewById(R.id.prerequisiteC);
        add = findViewById(R.id.addC);

        add.setOnClickListener(v -> checkValidation());

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
        } else if (credit.isEmpty()) {
            cCredit.setError("Enter Course Credit");
            cCredit.requestFocus();
        } else {
            if (prerequisite.isEmpty()) {
                prerequisite ="-";
                pd.setMessage("Adding...");
                pd.show();
                uploadData();
            }
            else {
                pd.setMessage("Adding...");
                pd.show();
                uploadData();
            }
        }
    }

    private void uploadData() {
        dbRef = reference;
        final String uniqueKey = dbRef.push().getKey();

        CourseData courseOfferData = new CourseData(code, title, credit, prerequisite, uniqueKey);

        dbRef.child(code).setValue(courseOfferData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
            cCode.setText("");
            cTitle.setText("");
            cCredit.setText("");
            cPrerequisite.setText("");

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