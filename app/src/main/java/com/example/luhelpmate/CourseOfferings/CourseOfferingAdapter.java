package com.example.luhelpmate.CourseOfferings;

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

import com.example.luhelpmate.Advisor.AdvisorEditActivity;
import com.example.luhelpmate.CR.CrEditActivity;
import com.example.luhelpmate.Faculty.FacultyEditActivity;
import com.example.luhelpmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class CourseOfferingAdapter extends RecyclerView.Adapter<CourseOfferingAdapter.ViewHolder>  {
    private ArrayList<CourseOfferingsData> list;
    private final Context context;

    public CourseOfferingAdapter(ArrayList<CourseOfferingsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView code, title, credit, prerequisite, teacherInitial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.cCode);
            title = itemView.findViewById(R.id.cTitle);
            credit = itemView.findViewById(R.id.cCredit);
            prerequisite = itemView.findViewById(R.id.cPrerequisite);
            teacherInitial = itemView.findViewById(R.id.teacherInitial);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offercourse_item, parent, false);
        return new CourseOfferingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        CourseOfferingsData model = list.get(position);
        holder.code.setText(model.getCode());
        holder.title.setText(model.getTitle());
        holder.credit.setText(model.getCredit());
        holder.prerequisite.setText(model.getPrerequisite());
        holder.teacherInitial.setText(model.getInitial());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()){
                    if (value.getString("admin").equals("1")){
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                                popupMenu.inflate(R.menu.faculty_menu);
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(item -> {
                                    switch (item.getItemId()) {
                                        case R.id.edit:
                                            Intent intent = new Intent(holder.itemView.getContext(), EditCourseOfferings.class);

                                            intent.putExtra("semester", model.getSemester());
                                            intent.putExtra("code", model.getCode());
                                            intent.putExtra("initial", model.getInitial());
                                            intent.putExtra("key", model.getKey());
                                            holder.itemView.getContext().startActivity(intent);
                                            break;

                                        case R.id.delete:
                                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                                            builder.setTitle("Are You Sure?");
                                            builder.setPositiveButton("Delete", (dialog, which) -> {
                                                FirebaseDatabase.getInstance().getReference().child("Course Offerings").child(model.getSemester()).child(model.getKey()).removeValue();
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
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<CourseOfferingsData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }

}
