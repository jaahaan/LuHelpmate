package com.example.luhelpmate.Routine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.R;
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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Make_Routine extends AppCompatActivity {

    private DatabaseReference dayRef, batchRef, codeRef, timeRef, initialRef, referenceT, referenceS, dbRef;
    private AutoCompleteTextView addInitial, addBatch, addSection, addCourseCode, addRoom;
    private Spinner daySpinner, timeSlotSpinner, departmentSpinner;
    private Button update;
    private String initial, day, timeSlot, department, batch, section, code, room;
    String[] deptItems = new String[]{"Select Department", "CSE", "EEE", "ARCHI", "CE", "BuA", "ENG", "LAW", "IS", "BNG", "THM", "PH"};
    private ProgressDialog pd;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_routine);

        dayRef = FirebaseDatabase.getInstance().getReference().child("Day");
        initialRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        batchRef = FirebaseDatabase.getInstance().getReference().child("Batch List");
        timeRef = FirebaseDatabase.getInstance().getReference().child("Time Slot");
        codeRef = FirebaseDatabase.getInstance().getReference().child("Course List");
        referenceT = FirebaseDatabase.getInstance().getReference().child("Teacher Routine");
        referenceS = FirebaseDatabase.getInstance().getReference().child("Student Routine");

        pd = new ProgressDialog(this);

        addInitial = findViewById(R.id.teacherInitial);
        daySpinner = findViewById(R.id.day);
        timeSlotSpinner = findViewById(R.id.timeSlot);
        departmentSpinner = findViewById(R.id.department);
        addBatch = findViewById(R.id.batch);
        addSection = findViewById(R.id.section);
        addCourseCode = findViewById(R.id.code);
        addRoom = findViewById(R.id.room);
        progressBar = findViewById(R.id.progressBar);
        update = findViewById(R.id.update);

        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSlot = timeSlotSpinner.getSelectedItem().toString();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = daySpinner.getSelectedItem().toString();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        departmentSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, deptItems));
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = departmentSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(v -> checkValidation());

        dayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchDay(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchTime(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        batchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchBatch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        codeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchCode(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        initialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchTeacher(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchDay(DataSnapshot snapshot) {
        ArrayList<String> day = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String dayData = dataSnapshot.child("day").getValue(String.class);
                day.add(dayData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, day);
            daySpinner.setAdapter(arrayAdapter);
        }
    }

    private void searchTime(DataSnapshot snapshot) {
        ArrayList<String> time = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String timeData = dataSnapshot.child("time").getValue(String.class);
                time.add(timeData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, time);
            timeSlotSpinner.setAdapter(arrayAdapter);
        }
    }

    private void searchTeacher(DataSnapshot snapshot) {
        ArrayList<String> teachers = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String teacherData = dataSnapshot.child("initial").getValue(String.class);
                teachers.add(teacherData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, teachers);
            addInitial.setAdapter(arrayAdapter);
            addInitial.setThreshold(1);
        }
    }

    private void searchCode(DataSnapshot snapshot) {
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

    private void searchBatch(DataSnapshot snapshot) {
        ArrayList<String> batch = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String batchData = dataSnapshot.child("batch").getValue(String.class);
                batch.add(batchData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, batch);
            addBatch.setAdapter(arrayAdapter);
            addBatch.setThreshold(1);
        }
    }

    private void checkValidation() {
        initial = addInitial.getText().toString();
        batch = addBatch.getText().toString();
        section = addSection.getText().toString();
        code = addCourseCode.getText().toString();
        room = addRoom.getText().toString();
        Pattern p = Pattern.compile("[a-z A-Z.]+");
        if (initial.isEmpty()) {
            addInitial.setError("Enter Semester");
            addInitial.requestFocus();
        } else if (!p.matcher(initial).matches()) {
            addInitial.setError("Initial can be only Alphabet");
            addInitial.requestFocus();
        } else if (day.equals("Day")) {
            Toast.makeText(getApplicationContext(), "Please Select Day", Toast.LENGTH_SHORT).show();
            return;
        } else if (timeSlot.equals("Time Slots")) {
            Toast.makeText(getApplicationContext(), "Please Select Time Slot", Toast.LENGTH_SHORT).show();
            return;
        } else if (department.equals("Select Department")) {
            Toast.makeText(getApplicationContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
            return;
        } else if (batch.isEmpty()) {
            addBatch.setError("Enter batch");
            addBatch.requestFocus();
        } else if (code.isEmpty()) {
            addCourseCode.setError("Enter Course Code");
            addCourseCode.requestFocus();
        } else if (room.isEmpty()) {
            addRoom.setError("Enter Room");
            addRoom.requestFocus();
        } else {
            pd.setMessage("Updating...");
            pd.show();
            uploadData();
        }
    }

    private void uploadData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    //For digits
                    Pattern digit = Pattern.compile("[\\d]+");
                    Matcher matcherDigit = digit.matcher(value.getString("initial"));

                    //For Alphabet
                    Pattern letter = Pattern.compile("[A-Z]");
                    Matcher matcherLetter = letter.matcher(value.getString("initial"));
                    if (matcherDigit.find()) {
                        if (matcherLetter.find()) {
                            dbRef = referenceS.child(batch).child(section).child(day);
                            final String uniqueKey = dbRef.push().getKey();
                            RoutineData data = new RoutineData(initial, day, timeSlot, department, batch, section, code, room, uniqueKey);
                            dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                timeRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        searchTime(snapshot);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                addInitial.setText("");
                                addCourseCode.setText("");
                                addRoom.setText("");
                            }).addOnFailureListener(e -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            });

                        } else {
                            dbRef = referenceS.child(batch).child(day);

                            final String uniqueKey = dbRef.push().getKey();
                            RoutineData data = new RoutineData(initial, day, timeSlot, department, batch, section, code, room, uniqueKey);
                            dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                addInitial.setText("");
                                addCourseCode.setText("");
                                addRoom.setText("");
                            }).addOnFailureListener(e -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        dbRef = referenceT.child(initial).child(day);
                        final String uniqueKey = dbRef.push().getKey();
                        RoutineData data = new RoutineData(initial, day, timeSlot, department, batch, section, code, room, uniqueKey);
                        dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            timeRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    searchTime(snapshot);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            departmentSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, deptItems));
                            addBatch.setText("");
                            addSection.setText("");
                            addCourseCode.setText("");
                            addRoom.setText("");
                        }).addOnFailureListener(e -> {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        });
    }
}