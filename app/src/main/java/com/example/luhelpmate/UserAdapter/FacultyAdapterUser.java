package com.example.luhelpmate.UserAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacultyAdapterUser extends RecyclerView.Adapter<FacultyAdapterUser.ViewHolder> {

    private ArrayList<FacultyData> list;
    private final Context context;

    public FacultyAdapterUser(ArrayList<FacultyData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, initial, designation, phone, email, txtOption;
        ImageView image, callIcon, emailIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.facultyName);
            initial = itemView.findViewById(R.id.facultyInitial);
            designation = itemView.findViewById(R.id.designation);
            phone = itemView.findViewById(R.id.facultyContact);
            callIcon = itemView.findViewById(R.id.callIcon);
            email = itemView.findViewById(R.id.facultyEmail);
            emailIcon = itemView.findViewById(R.id.emailIcon);
            image = itemView.findViewById(R.id.facultyImage);
            txtOption = itemView.findViewById(R.id.txtOption);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        FacultyData model = list.get(position);
        holder.name.setText(model.getName());
        holder.initial.setText(model.getInitial());
        holder.designation.setText(model.getDesignation());
        holder.phone.setText(model.getPhone());
        holder.email.setText(model.getEmail());
        Glide.with(holder.image).load(model.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(holder.image);


        holder.callIcon.setOnClickListener(v -> {
            Pattern p = Pattern.compile("[0][1][0-9]{9}");
            Matcher m = p.matcher(model.getPhone());
            if (m.find()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + m.group()));
                holder.phone.getContext().startActivity(dialIntent);
            }
        });

        holder.emailIcon.setOnClickListener(v -> {
            Matcher e = Patterns.EMAIL_ADDRESS.matcher(model.getEmail());
            if (e.find()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + e.group())); // only email apps should handle this
                holder.email.getContext().startActivity(Intent.createChooser(emailIntent, "Complete action using"));
            }
        });

        holder.txtOption.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<FacultyData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}

