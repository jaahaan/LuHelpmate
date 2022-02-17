package com.example.luhelpmate.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.luhelpmate.Data.CourseOfferingsData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OfferCourseActivity extends AppCompatActivity {


    DatabaseReference bRef, sRef, cRef, tRef, reference, dbRef;
    AutoCompleteTextView addBatch, addSemester, addCourseCode, addTitle, addPrequisite, addCredit, teacherInitial;
    private ProgressDialog pd;
    private Button update;
    private String batch, semester, code, title, credit, pre, initial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_course);

        tRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        bRef = FirebaseDatabase.getInstance().getReference().child("Batch List");
        sRef = FirebaseDatabase.getInstance().getReference().child("Semester");
        cRef = FirebaseDatabase.getInstance().getReference().child("Course List");

        reference = FirebaseDatabase.getInstance().getReference().child("Course Offering");

        pd = new ProgressDialog(this);

        addSemester = findViewById(R.id.addSemester);
        addBatch = findViewById(R.id.addBatch);
        addCourseCode = findViewById(R.id.codeC);
        addTitle = findViewById(R.id.titleC);
        addCredit = findViewById(R.id.creditC);
        addPrequisite = findViewById(R.id.prerequisiteC);
        teacherInitial = findViewById(R.id.teacherInitial);
        update = findViewById(R.id.update);

        update.setOnClickListener(v -> checkValidation());

        bRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchBatch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchSemester(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchCourse(snapshot);
                searchTitle(snapshot);
                searchPre(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        ArrayList<String> code = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("code").getValue(String.class);
                code.add(courseCode);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, code);
            addCourseCode.setAdapter(codeAdapter);
            addCourseCode.setThreshold(1);
        }
    }

    private void searchTitle(DataSnapshot snapshot) {
        ArrayList<String> code = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("title").getValue(String.class);
                code.add(courseCode);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, code);
            addTitle.setAdapter(codeAdapter);
            addTitle.setThreshold(1);
        }
    }

    private void searchPre(DataSnapshot snapshot) {
        ArrayList<String> pre = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String coursePre = dataSnapshot.child("prerequisite").getValue(String.class);
                pre.add(coursePre);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, pre);
            addPrequisite.setAdapter(codeAdapter);
            addPrequisite.setThreshold(1);
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

    private void searchBatch(DataSnapshot snapshot) {
        ArrayList<String> batchs = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String batchData = dataSnapshot.child("batch").getValue(String.class);
                batchs.add(batchData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, batchs);
            addBatch.setAdapter(arrayAdapter);
            addBatch.setThreshold(1);
        }
    }

    private void checkValidation() {
        batch = addBatch.getText().toString();
        semester = addSemester.getText().toString();
        code = addCourseCode.getText().toString();
        title = addTitle.getText().toString();
        credit = addCredit.getText().toString();
        pre = addPrequisite.getText().toString();
        initial = teacherInitial.getText().toString();
        if (semester.isEmpty()) {
            addSemester.setError("Enter Semester");
            addSemester.requestFocus();
        } else if (batch.isEmpty()) {
            addBatch.setError("Enter batch");
            addBatch.requestFocus();
        } else if (code.isEmpty()) {
            addCourseCode.setError("Enter Course Code");
            addCourseCode.requestFocus();
        } else if (title.isEmpty()) {
            addTitle.setError("Enter Course Title");
            addTitle.requestFocus();
        } else if (credit.isEmpty()) {
            addCredit.setError("Enter Course Credit");
            addCredit.requestFocus();
        } else if (initial.isEmpty()) {
            teacherInitial.setError("Enter Teacher Initial");
            teacherInitial.requestFocus();
        } else if (pre.isEmpty()) {
            pre = "-";
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
            addCourseCode.requestFocus();
        } else {
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
            addCourseCode.requestFocus();
        }
    }

    private void uploadData() {
        dbRef = reference.child(semester);
        final String uniqueKey = dbRef.push().getKey();
        CourseOfferingsData courseOfferData = new CourseOfferingsData(code, title, credit, pre, semester, batch, initial, uniqueKey);

        dbRef.child(uniqueKey).setValue(courseOfferData).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            addCourseCode.setText("");
            addTitle.setText("");
            addCredit.setText("");
            addPrequisite.setText("");
            teacherInitial.setText("");
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        });
    }
}
