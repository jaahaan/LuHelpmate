package com.example.luhelpmate.Routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Sun_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<RoutineData> list;
    private DatabaseReference reference;
    private FirebaseFirestore firestore;
    RoutineAdapter adapter;
    private ProgressBar progressBar;
    private TextView textView;

    public Sun_Fragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.routine_fragment, container, false);

        recyclerView = root.findViewById(R.id.recycler);
        progressBar = root.findViewById(R.id.progressBar);
        textView = root.findViewById(R.id.textView);

        firestore = FirebaseFirestore.getInstance();

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
                            reference = FirebaseDatabase.getInstance().getReference().child("Student Routine");
                            reference.child(matcherDigit.group()).child(matcherLetter.group()).child("SUNDAY").orderByChild("timeSlot").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list = new ArrayList<>();

                                    if (!snapshot.exists()) {
                                        recyclerView.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);

                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            RoutineData data = snapshot1.getValue(RoutineData.class);
                                            list.add(data);
                                        }
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        adapter = new RoutineAdapter(list, getContext());
                                        recyclerView.setAdapter(adapter);
                                        progressBar.setVisibility(View.GONE);
                                        textView.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            reference = FirebaseDatabase.getInstance().getReference().child("Student Routine");
                            reference.child(matcherDigit.group()).child("SUNDAY").orderByChild("timeSlot").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    list = new ArrayList<>();

                                    if (!snapshot.exists()) {
                                        recyclerView.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);

                                    } else {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            RoutineData data = snapshot1.getValue(RoutineData.class);
                                            list.add(data);
                                        }
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        adapter = new RoutineAdapter(list, getContext());
                                        recyclerView.setAdapter(adapter);
                                        progressBar.setVisibility(View.GONE);
                                        textView.setVisibility(View.GONE);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Teacher Routine");
                        reference.child(value.getString("initial")).child("SUNDAY").orderByChild("timeSlot").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list = new ArrayList<>();

                                if (!snapshot.exists()) {
                                    recyclerView.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);

                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        RoutineData data = snapshot1.getValue(RoutineData.class);
                                        list.add(data);
                                    }
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    adapter = new RoutineAdapter(list, getContext());
                                    recyclerView.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                    textView.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }
        });


        return root;
    }

}
