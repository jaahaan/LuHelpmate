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

import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.Data.CourseOfferingsData;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class CourseOfferingAdapter extends RecyclerView.Adapter<CourseOfferingAdapter.ViewHolder>  {
    private ArrayList<CourseOfferingsData> list;
    private final Context context;

    public CourseOfferingAdapter(ArrayList<CourseOfferingsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView code, title, credit, prerequisite;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.cCode);
            title = itemView.findViewById(R.id.cTitle);
            credit = itemView.findViewById(R.id.cCredit);
            prerequisite = itemView.findViewById(R.id.cPrerequisite);
            delete = itemView.findViewById(R.id.delete);

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
        CourseOfferingsData item = list.get(position);
        holder.code.setText(item.getCode());
        holder.title.setText(item.getTitle());
        holder.credit.setText(item.getCredit());
        holder.prerequisite.setText(item.getPrerequisite());
        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.code.getContext());
            builder.setTitle("Are You Sure?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                FirebaseDatabase.getInstance().getReference().child("Course Offering").child(item.getSemester()).removeValue();
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
            builder.show();
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
