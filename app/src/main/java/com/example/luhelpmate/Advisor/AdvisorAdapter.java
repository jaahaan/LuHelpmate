package com.example.luhelpmate.Advisor;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class AdvisorAdapter extends RecyclerView.Adapter<AdvisorAdapter.ViewHolder> {

    private ArrayList<AdvisorData> list;
    private final Context context;

    public AdvisorAdapter(ArrayList<AdvisorData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView batch, section, name1, name2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            batch = itemView.findViewById(R.id.batch);
            section = itemView.findViewById(R.id.section);
            name1 = itemView.findViewById(R.id.name1);
            name2 = itemView.findViewById(R.id.name2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advisor_item, parent, false);
        return new AdvisorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AdvisorData model = list.get(position);
        holder.batch.setText(model.getBatch());
        holder.section.setText(model.getSection());
        holder.name1.setText(model.getName1());
        holder.name2.setText(model.getName2());
        if (holder.section.getText().toString().equals("")){
            holder.section.setVisibility(View.GONE);
        }
        if (holder.name2.getText().toString().equals("")){
            holder.name2.setVisibility(View.GONE);
        }

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
                                            Intent intent = new Intent(holder.itemView.getContext(), AdvisorEditActivity.class);

                                            intent.putExtra("batch", model.getBatch());
                                            intent.putExtra("section", model.getSection());
                                            intent.putExtra("name1", model.getName1());
                                            intent.putExtra("name2", model.getName2());

                                            intent.putExtra("key", model.getKey());
                                            holder.itemView.getContext().startActivity(intent);
                                            break;

                                        case R.id.delete:
                                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                                            builder.setTitle("Are You Sure?");
                                            builder.setPositiveButton("Delete", (dialog, which) -> {
                                                FirebaseDatabase.getInstance().getReference().child("Advisor List").child(model.getKey()).removeValue();
                                                //FirebaseStorage.getInstance().getReference("Faculty").child(model.getImage()).delete();
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

    public void setFilter(ArrayList<AdvisorData> newlist){
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}

