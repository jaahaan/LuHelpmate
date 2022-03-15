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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class StudentFragment extends Fragment {

    private EditText name, id, batch, section, email;
    private Spinner departmentSpinner;
    private String department;
    private Pattern n = Pattern.compile("[a-z A-Z.]+");
    private Pattern e = Pattern.compile("(cse_)[\\d]{10}(@lus.ac.bd)");

    private Button update;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_student, container, false);

        firestore = FirebaseFirestore.getInstance();
        progressBar = root.findViewById(R.id.progressBar);

        name = root.findViewById(R.id.sname);
        id = root.findViewById(R.id.sId);
        batch = root.findViewById(R.id.sBatch);
        section = root.findViewById(R.id.sSection);
        departmentSpinner = root.findViewById(R.id.dept);
        email = root.findViewById(R.id.semail);

        String[] items = new String[]{"Select Department", "CSE", "EEE", "ARCHI", "CE", "BuA", "ENG", "LAW", "IS", "BNG", "THM", "PH"};
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
        update = root.findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    name.setError("Empty");
                    name.requestFocus();
                    return;
                } else if (!n.matcher(name.getText().toString()).matches()) {
                    name.setError("Name can be only Alphabet");
                    name.requestFocus();
                    return;
                } else if (id.getText().toString().isEmpty()) {
                    id.setError("Empty");
                    id.requestFocus();
                    return;
                } else if (id.getText().toString().length()!=10) {
                    id.setError("Id must be 10 digits");
                    id.requestFocus();
                    return;
                } else if (batch.getText().toString().isEmpty()) {
                    batch.setError("Empty");
                    batch.requestFocus();
                    return;
                } else if (department.equals("Select Department")) {
                    Toast.makeText(getContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Empty");
                    email.requestFocus();
                    return;
                } else if (!e.matcher(email.getText().toString()).matches()) {
                    email.setError("This is not a Student Email");
                    email.requestFocus();
                    return;
                } else if (!email.getText().toString().equals(signInAccount.getEmail())) {
                    email.setError("You Can't Change Your Email");
                    email.findFocus();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference df = firestore.collection("Users").document(user.getUid());
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            DocumentReference df = firestore.collection("Users").document(user.getUid());
                            Map<String, String> userInfo = new HashMap<>();
                            userInfo.put("email", signInAccount.getEmail());
                            userInfo.put("image", signInAccount.getPhotoUrl().toString());
                            userInfo.put("name", name.getText().toString().trim());
                            userInfo.put("designation", id.getText().toString().trim());
                            userInfo.put("initial", batch.getText().toString().trim() + section.getText().toString().trim());
                            userInfo.put("department", department);
                            userInfo.put("admin", "2");
                            userInfo.put("uid", user.getUid());
                            df.set(userInfo);
                            startActivity(new Intent(getContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        });
        return root;
    }

}