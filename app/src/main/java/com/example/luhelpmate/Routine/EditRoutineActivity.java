package com.example.luhelpmate.Routine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.luhelpmate.CourseOfferings.CourseOfferingsActivity;
import com.example.luhelpmate.R;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditRoutineActivity extends AppCompatActivity {

    private DatabaseReference dayRef, batchRef, codeRef, timeRef, initialRef, roomRef, referenceT, referenceS, dbRef;
    private AutoCompleteTextView addDay, addTimeSlot, addDepartment, addInitial, addBatch, addSection, addCourseCode, addRoom;
    private LinearLayout linearLayout;
    private TextInputLayout textInputLayout;
    private ProgressDialog pd;
    private Button update;
    private String initial, day, timeSlot, department, batch, section, code, room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        //For Autocomplete Suggestions
        dayRef = FirebaseDatabase.getInstance().getReference().child("Day");
        initialRef = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
        batchRef = FirebaseDatabase.getInstance().getReference().child("Batch List");
        timeRef = FirebaseDatabase.getInstance().getReference().child("Time Slot");
        codeRef = FirebaseDatabase.getInstance().getReference().child("Course List");
        roomRef = FirebaseDatabase.getInstance().getReference().child("Room");

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
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchRoom(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //For Autocomplete Suggestions

        referenceT = FirebaseDatabase.getInstance().getReference().child("Teacher Routine");
        referenceS = FirebaseDatabase.getInstance().getReference().child("Student Routine");

        //Finding AutoCompleteTextView by their Id
        addInitial = findViewById(R.id.teacherInitial);
        addDay = findViewById(R.id.day);
        addTimeSlot = findViewById(R.id.timeSlot);
        addDepartment = findViewById(R.id.department);
        linearLayout = findViewById(R.id.linear);
        textInputLayout = findViewById(R.id.i);
        addBatch = findViewById(R.id.batch);
        addSection = findViewById(R.id.section);
        addCourseCode = findViewById(R.id.code);
        addRoom = findViewById(R.id.room);
        update = findViewById(R.id.update);
        pd = new ProgressDialog(this);

        //Getting Exiting Texts from the database
        day = getIntent().getStringExtra("day");
        timeSlot = getIntent().getStringExtra("timeSlot");
        department = getIntent().getStringExtra("department");
        batch = getIntent().getStringExtra("batch");
        section = getIntent().getStringExtra("section");
        code = getIntent().getStringExtra("code");
        initial = getIntent().getStringExtra("initial");
        room = getIntent().getStringExtra("room");


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    //For digits
                    Pattern digit = Pattern.compile("[\\d]+");
                    Matcher matcherDigit = digit.matcher(value.getString("initial"));

                    if (matcherDigit.find()) {
                        linearLayout.setVisibility(View.GONE);
                        //Setting exiting texts into AutocompleteTextView
                        addDay.setText(day);
                        addTimeSlot.setText(timeSlot);
                        addDepartment.setText(department);
                        addCourseCode.setText(code);
                        addInitial.setText(initial);
                        addRoom.setText(room);
                    } else {
                        addInitial.setVisibility(View.GONE);
                        textInputLayout.setVisibility(View.GONE);
                        //Setting exiting texts into AutocompleteTextView
                        addDay.setText(day);
                        addTimeSlot.setText(timeSlot);
                        addDepartment.setText(department);
                        addBatch.setText(batch);
                        addSection.setText(section);
                        addCourseCode.setText(code);
                        addRoom.setText(room);
                    }
                }
            }
        });

        update.setOnClickListener(v -> uploadData());

    }

    private void uploadData() {
        Pattern p = Pattern.compile("[a-z A-Z.]+");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    day = addDay.getText().toString().trim();
                    timeSlot = addTimeSlot.getText().toString().trim();
                    department = addDepartment.getText().toString().trim();
                    batch = addBatch.getText().toString().trim();
                    section = addSection.getText().toString().trim();
                    initial = addInitial.getText().toString().trim();
                    code = addCourseCode.getText().toString().trim();
                    room = addRoom.getText().toString().trim();
                    Pattern digit = Pattern.compile("[\\d]+");
                    Pattern letter = Pattern.compile("[A-Z]+");
                    Matcher matcherDigit = digit.matcher(value.getString("initial"));
                    Matcher matcherLetter = letter.matcher(value.getString("initial"));
                    if (matcherDigit.find()) {
                        if (matcherLetter.find()) {
                            if (day.equals("Day")) {
                                Toast.makeText(getApplicationContext(), "Please Select Day", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (timeSlot.equals("Time Slots")) {
                                Toast.makeText(getApplicationContext(), "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (department.equals("Select Department")) {
                                Toast.makeText(getApplicationContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                                return;
                            } if (initial.isEmpty()) {
                                addInitial.setError("Enter Initial");
                                addInitial.requestFocus();
                            } else if (!p.matcher(initial).matches()) {
                                addInitial.setError("Initial can be only Alphabet");
                                addInitial.requestFocus();
                            } else if (code.isEmpty()) {
                                addCourseCode.setError("Enter Course Code");
                                addCourseCode.requestFocus();
                            } else if (room.isEmpty()) {
                                addRoom.setError("Enter Room");
                                addRoom.requestFocus();
                            } else {
                                pd.setMessage("Updating...");
                                pd.show();
                                dbRef = referenceS.child(matcherDigit.group()).child(matcherLetter.group()).child(day);
                                final String uniqueKey = getIntent().getStringExtra("key");
                                RoutineData data = new RoutineData(initial, day, timeSlot, department, matcherDigit.group(), matcherLetter.group(), code, room, uniqueKey);
                                dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }).addOnFailureListener(e -> {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            if (day.equals("Day")) {
                                Toast.makeText(getApplicationContext(), "Please Select Day", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (timeSlot.equals("Time Slots")) {
                                Toast.makeText(getApplicationContext(), "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (department.equals("Select Department")) {
                                Toast.makeText(getApplicationContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (initial.isEmpty()) {
                                addInitial.setError("Enter Initial");
                                addInitial.requestFocus();
                            } else if (!p.matcher(initial).matches()) {
                                addInitial.setError("Initial can be only Alphabet");
                                addInitial.requestFocus();
                            } else if (code.isEmpty()) {
                                addCourseCode.setError("Enter Course Code");
                                addCourseCode.requestFocus();
                            } else if (room.isEmpty()) {
                                addRoom.setError("Enter Room");
                                addRoom.requestFocus();
                            } else {
                                pd.setMessage("Updating...");
                                pd.show();
                                dbRef = referenceS.child(matcherDigit.group()).child(day);
                                final String uniqueKey = getIntent().getStringExtra("key");

                                RoutineData data = new RoutineData(initial, day, timeSlot, department, matcherDigit.group(), "", code, room, uniqueKey);
                                dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }).addOnFailureListener(e -> {
                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    } else {
                        if (day.equals("Day")) {
                            Toast.makeText(getApplicationContext(), "Please Select Day", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (timeSlot.equals("Time Slots")) {
                            Toast.makeText(getApplicationContext(), "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (department.equals("Select Department")) {
                            Toast.makeText(getApplicationContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (code.isEmpty()) {
                            addCourseCode.setError("Enter Course Code");
                            addCourseCode.requestFocus();
                        } else if (room.isEmpty()) {
                            addRoom.setError("Enter Room");
                            addRoom.requestFocus();
                        } else if (matcherLetter.find()){
                            pd.setMessage("Updating...");
                            pd.show();
                            dbRef = referenceT.child(matcherLetter.group()).child(day);
                            final String uniqueKey = getIntent().getStringExtra("key");
                            RoutineData data = new RoutineData(matcherLetter.group(), day, timeSlot, department, batch, section, code, room, uniqueKey);
                            dbRef.child(uniqueKey).setValue(data).addOnSuccessListener(unused -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }).addOnFailureListener(e -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }
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
            addDay.setAdapter(arrayAdapter);
            addDay.setThreshold(1);
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
            addTimeSlot.setAdapter(arrayAdapter);
            addTimeSlot.setThreshold(1);
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

    private void searchRoom(DataSnapshot snapshot) {
        ArrayList<String> room = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String roomData = dataSnapshot.child("room").getValue(String.class);
                room.add(roomData);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, room);
            addRoom.setAdapter(arrayAdapter);
            addRoom.setThreshold(1);
        }
    }

}