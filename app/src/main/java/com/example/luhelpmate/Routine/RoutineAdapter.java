package com.example.luhelpmate.Routine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private ArrayList<RoutineData> list;
    private final Context context;

    public RoutineAdapter(ArrayList<RoutineData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dept, batch, timeSlot, initial, code, room;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dept = itemView.findViewById(R.id.dept);
            batch = itemView.findViewById(R.id.batchSection);
            timeSlot = itemView.findViewById(R.id.timeSlot);
            initial = itemView.findViewById(R.id.initial);
            code = itemView.findViewById(R.id.code);
            room = itemView.findViewById(R.id.room);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        RoutineData model = list.get(position);
        holder.dept.setText(model.getDepartment());
        holder.batch.setText(model.getBatch() + " " + model.getSection());
        holder.timeSlot.setText(model.getTimeSlot());
        holder.initial.setText(model.getInitial());
        holder.code.setText(model.getCode());
        holder.room.setText(model.getRoom());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    Pattern p = Pattern.compile("[\\d]+");
                    Matcher m = p.matcher(value.getString("initial"));
                    if (m.find()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cr Info");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String data = dataSnapshot.child("email").getValue(String.class);
                                        if (value.getString("email").equals(data)) {
                                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                                                    popupMenu.inflate(R.menu.faculty_menu);
                                                    popupMenu.show();
                                                    popupMenu.setOnMenuItemClickListener(item -> {
                                                        switch (item.getItemId()) {
                                                            case R.id.edit:
                                                                Intent intent = new Intent(holder.itemView.getContext(), EditRoutineActivity.class);

                                                                intent.putExtra("day", model.getDay());
                                                                intent.putExtra("timeSlot", model.getTimeSlot());
                                                                intent.putExtra("department", model.getDepartment());
                                                                intent.putExtra("batch", model.getBatch());
                                                                intent.putExtra("section", model.getSection());
                                                                intent.putExtra("initial", model.getInitial());
                                                                intent.putExtra("code", model.getCode());
                                                                intent.putExtra("room", model.getRoom());
                                                                intent.putExtra("key", model.getKey());
                                                                holder.itemView.getContext().startActivity(intent);
                                                                break;

                                                            case R.id.delete:
                                                                //For Alphabet
                                                                Pattern letter = Pattern.compile("[A-Z]");
                                                                Matcher matcherLetter = letter.matcher(value.getString("initial"));
                                                                if (matcherLetter.find()) {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                                                                    builder.setTitle("Are You Sure?");
                                                                    builder.setPositiveButton("Delete", (dialog, which) -> {
                                                                        FirebaseDatabase.getInstance().getReference().child("Student Routine").child(model.getBatch()).child(model.getSection()).child(model.getDay()).child(model.getKey()).removeValue();
                                                                        Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                    });
                                                                    builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                                                                    builder.show();

                                                                } else {
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                                                                    builder.setTitle("Are You Sure?");
                                                                    builder.setPositiveButton("Delete", (dialog, which) -> {
                                                                        FirebaseDatabase.getInstance().getReference().child("Student Routine").child(model.getBatch()).child(model.getDay()).child(model.getKey()).removeValue();
                                                                        Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                    });
                                                                    builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                                                                    builder.show();
                                                                }
                                                                break;
                                                        }
                                                        return false;
                                                    });


                                                    return false;
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Faculty Info");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        String data = dataSnapshot.child("email").getValue(String.class);
                                        if (value.getString("email").equals(data)) {
                                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View v) {
                                                    PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                                                    popupMenu.inflate(R.menu.faculty_menu);
                                                    popupMenu.show();
                                                    popupMenu.setOnMenuItemClickListener(item -> {
                                                        switch (item.getItemId()) {
                                                            case R.id.edit:
                                                                Intent intent = new Intent(holder.itemView.getContext(), EditRoutineActivity.class);

                                                                intent.putExtra("day", model.getDay());
                                                                intent.putExtra("timeSlot", model.getTimeSlot());
                                                                intent.putExtra("department", model.getDepartment());
                                                                intent.putExtra("batch", model.getBatch());
                                                                intent.putExtra("section", model.getSection());
                                                                intent.putExtra("initial", model.getInitial());
                                                                intent.putExtra("code", model.getCode());
                                                                intent.putExtra("room", model.getRoom());
                                                                intent.putExtra("key", model.getKey());
                                                                holder.itemView.getContext().startActivity(intent);
                                                                break;

                                                            case R.id.delete:
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                                                                builder.setTitle("Are You Sure?");
                                                                builder.setPositiveButton("Delete", (dialog, which) -> {
                                                                    FirebaseDatabase.getInstance().getReference().child("Teacher Routine").child(model.getInitial()).child(model.getDay()).child(model.getKey()).removeValue();
                                                                    Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                });
                                                                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                                                                builder.show();
                                                                break;
                                                        }
                                                        return false;
                                                    });

                                                    return false;
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<RoutineData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}

