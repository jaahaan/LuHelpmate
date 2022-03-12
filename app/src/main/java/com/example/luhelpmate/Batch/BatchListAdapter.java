package com.example.luhelpmate.Batch;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;


import java.util.ArrayList;

public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.ViewHolder> {

    private ArrayList<BatchData> list;
    private final Context context;

    public BatchListAdapter(ArrayList<BatchData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView batch, section;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            batch = itemView.findViewById(R.id.batchList);
            section = itemView.findViewById(R.id.sectionList);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batchlist_item_layout, parent, false);
        return new BatchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        BatchData item = list.get(position);
        holder.batch.setText(item.getBatch());
        holder.section.setText(item.getSection());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Batch "+holder.batch.getText().toString()+"?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("Batch List").child(item.getBatch()).removeValue();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                builder.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<BatchData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }

}
