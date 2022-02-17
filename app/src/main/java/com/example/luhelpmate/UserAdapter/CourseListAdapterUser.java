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

import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class CourseListAdapterUser extends RecyclerView.Adapter<CourseListAdapterUser.ViewHolder> {


    private ArrayList<CourseData> list;
    private final Context context;

    public CourseListAdapterUser(ArrayList<CourseData> list, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courselist_item_layout, parent, false);
        return new CourseListAdapterUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, final int position) {
        CourseData item = list.get(position);
        holder.code.setText(item.getCode());
        holder.title.setText(item.getTitle());
        holder.credit.setText(item.getCredit());
        holder.prerequisite.setText(item.getPrerequisite());
        holder.delete.setVisibility(View.GONE);
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