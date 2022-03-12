package com.example.luhelpmate.Routine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.CourseList.CourseList;
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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditRoutineActivity extends AppCompatActivity {

    private DatabaseReference dayRef, batchRef, codeRef, timeRef, initialRef, referenceT, referenceS, dbRef;
    private AutoCompleteTextView addDay, addTimeSlot, addDepartment, addInitial, addBatch, addSection, addCourseCode, addRoom;
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
        //For Autocomplete Suggestions

        referenceT = FirebaseDatabase.getInstance().getReference().child("Teacher Routine");
        referenceS = FirebaseDatabase.getInstance().getReference().child("Student Routine");

        //Finding AutoCompleteTextView by their Id
        addInitial = findViewById(R.id.teacherInitial);
        addDay = findViewById(R.id.day);
        addTimeSlot = findViewById(R.id.timeSlot);
        addDepartment = findViewById(R.id.department);
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

        //Setting exiting texts into AutocompleteTextView
        addDay.setText(day);
        addTimeSlot.setText(timeSlot);
        addDepartment.setText(department);
        addBatch.setText(batch);
        addSection.setText(section);
        addCourseCode.setText(code);
        addInitial.setText(initial);
        addRoom.setText(room);

        update.setOnClickListener(v -> checkValidation());

    }


    private void checkValidation() {
        day = addDay.getText().toString();
        timeSlot = addTimeSlot.getText().toString();
        department = addDepartment.getText().toString();
        batch = addBatch.getText().toString();
        section = addSection.getText().toString();
        initial = addInitial.getText().toString();
        code = addCourseCode.getText().toString();
        room = addRoom.getText().toString();

        Pattern p = Pattern.compile("[a-z A-Z.]+");
        if (day.isEmpty()) {
            addDay.setError("Enter Day");
            addDay.requestFocus();
        } else if (timeSlot.isEmpty()) {
            addTimeSlot.setError("Enter Slot");
            addTimeSlot.requestFocus();
        } else if (department.isEmpty()) {
            addDepartment.setError("Enter Department");
            addDepartment.requestFocus();
        } else if (batch.isEmpty()) {
            addBatch.setError("Enter batch");
            addBatch.requestFocus();
        } else if (initial.isEmpty()) {
            addInitial.setError("Enter Semester");
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


                    Pattern d = Pattern.compile("[\\d]+");
                    Pattern p = Pattern.compile("[A-Z]");
                    Matcher dm = d.matcher(value.getString("initial"));
                    Matcher pm = p.matcher(value.getString("initial"));
                    if (dm.find()) {
                        if (pm.find()) {
                            dbRef = referenceS.child(batch).child(section).child(day);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("day", day);
                            map.put("timeSlot", timeSlot);
                            map.put("department", department);
                            map.put("batch", batch);
                            map.put("section", section);
                            map.put("initial", initial);
                            map.put("code", code);
                            map.put("room", room);
                            String uniqueKey = getIntent().getStringExtra("key");
                            map.put("key", uniqueKey);

                            dbRef.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }).addOnFailureListener(e -> {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            dbRef = referenceS.child(batch).child(day);

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("day", day);
                            map.put("slot", timeSlot);
                            map.put("department", department);
                            map.put("batch", batch);
                            map.put("section", section);
                            map.put("initial", initial);
                            map.put("code", code);
                            map.put("room", room);
                            String uniqueKey = getIntent().getStringExtra("key");
                            map.put("key", uniqueKey);

                            dbRef.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
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
                        dbRef = referenceT.child(initial).child(day);

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("day", day);
                        map.put("slot", timeSlot);
                        map.put("department", department);
                        map.put("batch", batch);
                        map.put("section", section);
                        map.put("initial", initial);
                        map.put("code", code);
                        map.put("room", room);
                        String uniqueKey = getIntent().getStringExtra("key");
                        map.put("key", uniqueKey);

                        dbRef.child(uniqueKey).setValue(map).addOnSuccessListener(unused -> {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            //daySpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dayItems));
                            timeRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    searchTime(snapshot);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }).addOnFailureListener(e -> {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });

                    }
                }
            }
        });
    }

    private void searchDay(DataSnapshot snapshot) {
        ArrayList<String> day = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String dayData = dataSnapshot.child("time").getValue(String.class);
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
}