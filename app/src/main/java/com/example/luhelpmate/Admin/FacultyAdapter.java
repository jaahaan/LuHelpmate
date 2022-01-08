package com.example.luhelpmate.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Data.FacultyData;
import com.example.luhelpmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacultyAdapter extends ArrayAdapter<FacultyData> {

    public FacultyAdapter(Context context, List<FacultyData> facultyData) {
        super(context, 0, facultyData);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.faculty_item_layout, parent, false);
        }


        // Find the cr at the given position in the list
        FacultyData item = getItem(position);

        TextView name = listItemView.findViewById(R.id.facultyName);
        name.setText(item.getName());

        TextView initial = listItemView.findViewById(R.id.facultyInitial);
        initial.setText(item.getAcronym());

        TextView designation = listItemView.findViewById(R.id.designation);
        designation.setText(item.getDesignation());

        TextView phone = listItemView.findViewById(R.id.facultyContact);
        phone.setText(item.getPhone());

        TextView email = listItemView.findViewById(R.id.facultyEmail);
        email.setText(item.getEmail());

        ImageView imageView = listItemView.findViewById(R.id.facultyImage);
        Glide.with(imageView.getContext()).load(item.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(imageView);

        /**ImageView editF = listItemView.findViewById(R.id.editF);
        ImageView deleteFaculty = listItemView.findViewById(R.id.deleteFaculty);

        editF.setVisibility(View.GONE);
        deleteFaculty.setVisibility(View.GONE);*/


        /**ImageView btnEdit = listItemView.findViewById(R.id.editF);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(imageView.getContext()).setContentHolder(new ViewHolder(R.layout.activity_faculty_update_popup)).setExpanded(true, 1200).create();
                View view = dialogPlus.getHeaderView();
                final EditText name = view.findViewById(R.id.updatefacultyName);
                final EditText initial = view.findViewById(R.id.updateFacultyAcronym);
                final EditText designation = view.findViewById(R.id.updateDesignation);
                final EditText phone = view.findViewById(R.id.updatefacultyPhn);
                final EditText email = view.findViewById(R.id.updatefacultyEmail);
                final ImageView imageView = view.findViewById(R.id.updateFacultyImg);

                ImageView btnUpdate = view.findViewById(R.id.updateFaculty);

                name.setText(item.getName());
                initial.setText(item.getAcronym());
                designation.setText(item.getDesignation());
                phone.setText(item.getPhone());
                email.setText(item.getEmail());
                Glide.with(imageView.getContext()).load(item.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(imageView);

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("acronym", initial.getText().toString());
                        map.put("designation", designation.getText().toString());
                        map.put("phone", phone.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("image", imageView.getContext().toString());

                        FirebaseDatabase.getInstance().getReference().child("Faculty Info").child(getItem(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(name.getContext(), "Error", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        });
                    }
                });

            }
        });*/
        ImageView btndelete = listItemView.findViewById(R.id.deleteFaculty);
        return listItemView;
    }
}
