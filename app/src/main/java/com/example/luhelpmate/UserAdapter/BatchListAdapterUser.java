package com.example.luhelpmate.UserAdapter;

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

import com.example.luhelpmate.Data.BatchData;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class BatchListAdapterUser extends RecyclerView.Adapter<BatchListAdapterUser.ViewHolder> {

    private ArrayList<BatchData> list;
    private final Context context;

    public BatchListAdapterUser(ArrayList<BatchData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView batch, section;
        ImageView delete;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            batch = itemView.findViewById(R.id.batchList);
            section = itemView.findViewById(R.id.sectionList);
            delete = itemView.findViewById(R.id.deleteBatch);

        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batchlist_item_layout, parent, false);
        return new BatchListAdapterUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        BatchData item = list.get(position);
        holder.batch.setText(item.getBatch());
        holder.section.setText(item.getSection());
        holder.delete.setVisibility(View.GONE);
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
