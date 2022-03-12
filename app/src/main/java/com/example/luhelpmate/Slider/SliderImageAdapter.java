package com.example.luhelpmate.Slider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Advisor.AdvisorAdapter;
import com.example.luhelpmate.Advisor.AdvisorData;
import com.example.luhelpmate.Advisor.AdvisorEditActivity;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class SliderImageAdapter extends RecyclerView.Adapter<SliderImageAdapter.ViewHolder> {

    private ArrayList<SliderData> list;
    private final Context context;

    public SliderImageAdapter(ArrayList<SliderData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        SliderData item = list.get(position);
        Glide.with(holder.image).load(item.getImage()).placeholder(R.drawable.image_icon).error(R.drawable.image_icon).into(holder.image);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Image?");
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance().getReference().child("Slider Image").child(item.getKey()).removeValue();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                builder.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
