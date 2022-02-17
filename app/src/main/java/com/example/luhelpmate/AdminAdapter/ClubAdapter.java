package com.example.luhelpmate.AdminAdapter;

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
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Data.BatchData;
import com.example.luhelpmate.Data.ClubsData;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {

    private ArrayList<ClubsData> list;
    private final Context context;

    public ClubAdapter(ArrayList<ClubsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView clubName;
        ImageView clubImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            clubImage = itemView.findViewById(R.id.clubImage);
            clubName = itemView.findViewById(R.id.clubName);
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clubs_item, parent, false);
        return new ClubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClubAdapter.ViewHolder holder, final int position) {
        ClubsData item = list.get(position);
        holder.clubName.setText(item.getTitle());
        Glide.with(holder.clubImage).load(item.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(holder.clubImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<ClubsData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
