package com.example.luhelpmate.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luhelpmate.HomeActivity;
import com.example.luhelpmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TeacherFragment extends Fragment {
    private EditText name, initial, email;
    private Spinner designationSpinner, departmentSpinner;
    private String department, designation;
    private Pattern n = Pattern.compile("[a-z A-Z.]+");

    private Button update;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_teacher, container, false);

        progressBar = root.findViewById(R.id.progressBar);

        name = root.findViewById(R.id.name);
        initial = root.findViewById(R.id.initial);
        designationSpinner = root.findViewById(R.id.designation);
        departmentSpinner = root.findViewById(R.id.dept);
        email = root.findViewById(R.id.email);
        update = root.findViewById(R.id.update);
        firestore = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Designation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchDesignation(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        designationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation = designationSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] items = new String[]{"Department", "CSE", "EEE", "ARCHI", "CE", "BuA", "ENG", "LAW", "IS", "BNG", "THM", "PH"};
        departmentSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items));
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = departmentSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        name.setText(signInAccount.getDisplayName());
        email.setText(signInAccount.getEmail());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Empty");
                    name.requestFocus();
                } else if (!n.matcher(name.getText().toString()).matches()) {
                    name.setError("Name can be only Alphabet");
                    name.requestFocus();
                } else if (initial.getText().toString().isEmpty()) {
                    initial.setError("Empty");
                    initial.requestFocus();
                } else if (!n.matcher(initial.getText().toString()).matches()) {
                    initial.setError("Initial can be only Alphabet");
                    initial.requestFocus();
                } else if (designation.equals("Designation")) {
                    Toast.makeText(getContext(), "Please Select Designation", Toast.LENGTH_SHORT).show();
                    return;
                } else if (department.equals("Department")) {
                    Toast.makeText(getContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Empty");
                    email.requestFocus();
                } else if (!email.getText().toString().equals(signInAccount.getEmail())) {
                    email.setError("You Can't Change Your Email");
                    email.requestFocus();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
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
        });

        return root;
    }

    private void check(DataSnapshot snapshot) {
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("email").getValue(String.class);
                if (data.equals(email.getText().toString())) {
                    updateInfo();
                } else {
                    email.setError("This is not a Teacher Email");
                    email.requestFocus();
                }
            }
        }
    }
    private void searchDesignation(DataSnapshot snapshot) {
        ArrayList<String> list = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("designation").getValue(String.class);
                list.add(data);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
            designationSpinner.setAdapter(arrayAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void updateInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference df = firestore.collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    DocumentReference df = firestore.collection("Users").document(user.getUid());
                    Map<String, String> userInfo = new HashMap<>();
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
                    userInfo.put("email", signInAccount.getEmail());
                    userInfo.put("image", signInAccount.getPhotoUrl().toString());
                    userInfo.put("name", name.getText().toString());
                    userInfo.put("designation", designation);
                    userInfo.put("initial", initial.getText().toString());
                    userInfo.put("department", department);
                    userInfo.put("admin", "2");
                    userInfo.put("uid", user.getUid());
                    df.set(userInfo);
                    startActivity(new Intent(getContext(), HomeActivity.class));
                    getActivity().finish();
                    Toast.makeText(getContext(), "Info Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}