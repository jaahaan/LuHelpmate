package com.example.luhelpmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luhelpmate.User.HomeActivityUser;
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


public class StudentFragment extends Fragment {

    private EditText name, id, batch, section, email;
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
        email = root.findViewById(R.id.semail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        name.setText(signInAccount.getDisplayName());
        email.setText(signInAccount.getEmail());

        update = root.findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals(signInAccount.getEmail())) {
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
                            userInfo.put("name", name.getText().toString());
                            userInfo.put("designation", id.getText().toString());
                            userInfo.put("initial", batch.getText().toString() + section.getText().toString());
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

}