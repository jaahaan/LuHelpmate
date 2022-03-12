package com.example.luhelpmate.TimeSlot;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private ArrayList<TimeSlotData> list;
    private final Context context;

    public TimeSlotAdapter(ArrayList<TimeSlotData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView timeSlot;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.timeSlot);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item, parent, false);
        return new TimeSlotAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        TimeSlotData item = list.get(position);
        holder.timeSlot.setText(item.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Batch "+holder.timeSlot.getText().toString()+"?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("Time Slot").child(item.getTime()).removeValue();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<TimeSlotData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }

}
