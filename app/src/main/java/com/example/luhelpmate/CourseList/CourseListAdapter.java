package com.example.luhelpmate.CourseList;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {


    private ArrayList<CourseData> list;
    private final Context context;

    public CourseListAdapter(ArrayList<CourseData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView code, title, credit, prerequisite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.cCode);
            title = itemView.findViewById(R.id.cTitle);
            credit = itemView.findViewById(R.id.cCredit);
            prerequisite = itemView.findViewById(R.id.cPrerequisite);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courselist_item_layout, parent, false);
        return new CourseListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        CourseData model = list.get(position);
        holder.code.setText(model.getCode());
        holder.title.setText(model.getTitle());
        holder.credit.setText(model.getCredit());
        holder.prerequisite.setText(model.getPrerequisite());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (value.getString("admin").equals("1")) {
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                                popupMenu.inflate(R.menu.faculty_menu);
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(item -> {

                                    if (item.getItemId() == R.id.edit) {
                                        Intent intent = new Intent(holder.itemView.getContext(), EditCourseList.class);

                                        intent.putExtra("title", model.getTitle());
                                        intent.putExtra("code", model.getCode());
                                        intent.putExtra("credit", model.getCredit());
                                        intent.putExtra("prerequisite", model.getPrerequisite());
                                        intent.putExtra("key", model.getKey());
                                        holder.itemView.getContext().startActivity(intent);
                                    } else if (item.getItemId() == R.id.delete){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.code.getContext());
                                        builder.setTitle("Delete Course?");
                                        builder.setMessage("Title: "+ holder.title.getText().toString());
                                        builder.setPositiveButton("Delete", (dialog, which) -> {
                                            FirebaseDatabase.getInstance().getReference().child("Course List").child(model.getKey()).removeValue();
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                                        });
                                        builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                                        builder.show();
                                    }

                                    return false;
                                });
                                return true;
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

    public void setFilter(ArrayList<CourseData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }

}