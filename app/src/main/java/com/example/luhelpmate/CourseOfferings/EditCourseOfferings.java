package com.example.luhelpmate.CourseOfferings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditCourseOfferings extends AppCompatActivity {

    private DatabaseReference semesterRef, courseRef, teacherRef, reference, dbRef;
    private AutoCompleteTextView addSemester, addCourseCode, teacherInitial;
    private ProgressDialog pd;
    private Button update;
    private String semester, code, initial;
    private ProgressBar progressBar;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course_offerings);

        teacherRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        semesterRef = FirebaseDatabase.getInstance().getReference().child("Semester");
        courseRef = FirebaseDatabase.getInstance().getReference().child("Course List");

        reference = FirebaseDatabase.getInstance().getReference().child("Course Offerings");

        pd = new ProgressDialog(this);

        semester = getIntent().getStringExtra("semester");
        code = getIntent().getStringExtra("code");
        initial = getIntent().getStringExtra("initial");

        addSemester = findViewById(R.id.semester);
        addCourseCode = findViewById(R.id.code);
        teacherInitial = findViewById(R.id.teacherInitial);
        update = findViewById(R.id.update);

        progressBar = findViewById(R.id.progressBar);
        addSemester.setText(semester);
        addCourseCode.setText(code);
        teacherInitial.setText(initial);

        update.setOnClickListener(v -> checkValidation());

        semesterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchSemester(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchCourse(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchTeacher(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchTeacher(DataSnapshot snapshot) {
        ArrayList<String> teachers = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String teacherData = dataSnapshot.child("initial").getValue(String.class);
                teachers.add(teacherData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, teachers);
            teacherInitial.setAdapter(arrayAdapter);
            teacherInitial.setThreshold(1);
        }
    }

    private void searchCourse(DataSnapshot snapshot) {
        ArrayList<String> list1 = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("code").getValue(String.class);
                list1.add(courseCode);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list1);
            addCourseCode.setAdapter(codeAdapter);
            addCourseCode.setThreshold(1);
        }
    }

    private void searchSemester(DataSnapshot snapshot) {
        ArrayList<String> semesters = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String semesterData = dataSnapshot.child("semester").getValue(String.class);
                semesters.add(semesterData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, semesters);
            addSemester.setAdapter(arrayAdapter);
            addSemester.setThreshold(1);
        }
    }

    private void checkValidation() {
        code = addCourseCode.getText().toString().trim();
        initial = teacherInitial.getText().toString().trim();
        if (semester.equals("Semester")) {
            Toast.makeText(getApplicationContext(), "Select Semester", Toast.LENGTH_SHORT).show();
        } else if (code.isEmpty()) {
            addCourseCode.setError("Enter Course Code");
            addCourseCode.requestFocus();
        } /**else if (initial.isEmpty()) {
         initial = "-";
         pd.setMessage("Updating...");
         pd.show();
         }*/
        else {
            pd.setMessage("Updating...");
            pd.show();
            courseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    courseDetails(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void courseDetails(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("code").getValue(String.class);
                String title = dataSnapshot.child("title").getValue(String.class);
                String credit = dataSnapshot.child("credit").getValue(String.class);
                String prerequisite = dataSnapshot.child("prerequisite").getValue(String.class);
                if (data.equals(code)) {
                    flag = 1;
                    dbRef = reference.child(semester);
                    String uniqueKey = getIntent().getStringExtra("key");
                    CourseOfferingsData d = new CourseOfferingsData(code, title, credit, prerequisite, semester, initial, uniqueKey);

                    dbRef.child(uniqueKey).setValue(d).addOnSuccessListener(unused -> {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, CourseOfferingsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    });
                    break;
                }
            }
            if (flag == 0) {
                addCourseCode.setError("Wrong Course Code");
                addCourseCode.requestFocus();
                pd.dismiss();
                //Toast.makeText(getApplicationContext(), "Wrong Course Code", Toast.LENGTH_SHORT).show();
            }
        }
    }
}