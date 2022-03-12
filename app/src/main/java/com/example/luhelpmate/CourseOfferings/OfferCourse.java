package com.example.luhelpmate.CourseOfferings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OfferCourse extends AppCompatActivity {


    private DatabaseReference bRef, sRef, cRef, tRef, reference, dbRef;
    private AutoCompleteTextView addCourseCode, teacherInitial;
    private Spinner semesterSpinner, batchSpinner;
    private ProgressDialog pd;
    private Button update;
    private String batch, semester, code, initial;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_course);

        tRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        bRef = FirebaseDatabase.getInstance().getReference().child("Batch List");
        sRef = FirebaseDatabase.getInstance().getReference().child("Semester");
        cRef = FirebaseDatabase.getInstance().getReference().child("Course List");

        reference = FirebaseDatabase.getInstance().getReference().child("Course Offerings");

        pd = new ProgressDialog(this);

        semesterSpinner = findViewById(R.id.semester);
        batchSpinner = findViewById(R.id.batch);
        addCourseCode = findViewById(R.id.codeC);
        // addTitle = findViewById(R.id.titleC);
        // addCredit = findViewById(R.id.creditC);
        // addPrerequisite = findViewById(R.id.prerequisiteC);
        teacherInitial = findViewById(R.id.teacherInitial);
        update = findViewById(R.id.update);
        progressBar = findViewById(R.id.progressBar);

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

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = semesterSpinner.getSelectedItem().toString();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        batchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                batch = batchSpinner.getSelectedItem().toString();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();

        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String courseCode = dataSnapshot.child("code").getValue(String.class);
                String courseTitle = dataSnapshot.child("title").getValue(String.class);
                String coursePrerequisite = dataSnapshot.child("prerequisite").getValue(String.class);
                list1.add(courseCode);
                list2.add(courseTitle);
                list3.add(coursePrerequisite);
            }
            ArrayAdapter codeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list1);
            ArrayAdapter titleAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list2);
            ArrayAdapter preAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list3);
            addCourseCode.setAdapter(codeAdapter);
            addCourseCode.setThreshold(1);
            // addPrerequisite.setAdapter(preAdapter);
            // addPrerequisite.setThreshold(1);
            // addTitle.setAdapter(titleAdapter);
            // addTitle.setThreshold(1);
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
            semesterSpinner.setAdapter(arrayAdapter);
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
            batchSpinner.setAdapter(arrayAdapter);
        }
    }

    private void checkValidation() {

        code = addCourseCode.getText().toString().trim();
        // title = addTitle.getText().toString();
        // credit = addCredit.getText().toString();
        // pre = addPrerequisite.getText().toString();
        initial = teacherInitial.getText().toString().trim();
        if (semester.equals("Semester")) {
            Toast.makeText(getApplicationContext(), "Select Semester", Toast.LENGTH_SHORT).show();
        } else if (batch.equals("Batch")) {
            Toast.makeText(getApplicationContext(), "Select Batch", Toast.LENGTH_SHORT).show();
        } else if (code.isEmpty()) {
            addCourseCode.setError("Enter Course Code");
            addCourseCode.requestFocus();
        } /**else if (title.isEmpty()) {
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
         }*/
        else{
            pd.setMessage("Updating...");
            pd.show();
            //uploadData();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Course List");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    check(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void check(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("code").getValue(String.class);
                String title = dataSnapshot.child("title").getValue(String.class);
                String credit = dataSnapshot.child("credit").getValue(String.class);
                String prerequisite = dataSnapshot.child("prerequisite").getValue(String.class);
                if (data.equals(code)) {
                    dbRef = reference.child(semester);
                    final String uniqueKey = dbRef.push().getKey();
                    CourseOfferingsData d = new CourseOfferingsData(code, title, credit, prerequisite, semester, batch, initial, uniqueKey);

                    dbRef.child(uniqueKey).setValue(d).addOnSuccessListener(unused -> {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        addCourseCode.setText("");
                        teacherInitial.setText("");
                        // addTitle.setText("");
                        // addCredit.setText("");
                        // addPrerequisite.setText("");
                    }).addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    });
                    break;
                }
            }
        }
    }
}
