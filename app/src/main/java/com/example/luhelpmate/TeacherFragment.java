package com.example.luhelpmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luhelpmate.Admin.HomeActivityAdmin;
import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.Data.User;
import com.example.luhelpmate.Data.teacherData;
import com.example.luhelpmate.User.HomeActivityUser;
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


public class TeacherFragment extends Fragment {
    private EditText name, initial, email;

    AutoCompleteTextView designation;
    private Button update;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_teacher, container, false);

        progressBar = root.findViewById(R.id.progressBar);

        name = root.findViewById(R.id.name);
        initial = root.findViewById(R.id.initial);
        designation = root.findViewById(R.id.designation);
        email = root.findViewById(R.id.email);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        name.setText(signInAccount.getDisplayName());
        email.setText(signInAccount.getEmail());

        update = root.findViewById(R.id.update);
        firestore = FirebaseFirestore.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Designation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchDesignation(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().equals(signInAccount.getEmail())) {
                    email.setError("You Can't Change Your Email");
                    email.requestFocus();
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
                                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
                                    userInfo.put("email", signInAccount.getEmail());
                                    userInfo.put("image", signInAccount.getPhotoUrl().toString());
                                    userInfo.put("name", name.getText().toString());
                                    userInfo.put("designation", designation.getText().toString());
                                    userInfo.put("initial", initial.getText().toString());
                                    userInfo.put("admin", "2");
                                    userInfo.put("uid", user.getUid());
                                    df.set(userInfo);
                                    startActivity(new Intent(getContext(), HomeActivityUser.class));
                                    getActivity().finish();
                                    Toast.makeText(getContext(), "Account Created Successfully!!!", Toast.LENGTH_SHORT).show();
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

    private void searchDesignation(DataSnapshot snapshot) {
        ArrayList<String> list = new ArrayList<>();
        if (snapshot.exists()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                String data = dataSnapshot.child("designation").getValue(String.class);
                list.add(data);
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
            designation.setAdapter(arrayAdapter);
            designation.setThreshold(1);
        }
    }

}