package com.example.luhelpmate.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.Data.BatchData;
import com.example.luhelpmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.orhanobut.dialogplus.DialogPlus;


import java.util.ArrayList;
import java.util.LinkedList;

public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.ViewHolder> {

    private final LinkedList<BatchData> list;
    private final Context context;
    private DatabaseReference reference;
    private String uniqueKey;

    public BatchListAdapter(LinkedList<BatchData> list, Context context) {
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
        reference = FirebaseDatabase.getInstance().getReference().child("Batch List");
        return new BatchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        BatchData item = list.get(position);
        holder.batch.setText(item.getBatch());
        holder.section.setText(item.getSection());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }


}
