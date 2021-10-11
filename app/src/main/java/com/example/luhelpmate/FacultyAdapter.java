package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class FacultyAdapter extends ArrayAdapter<Object> {

    public FacultyAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_faculty, parent, false);
        }

        // Find the book at the given position in the list of faculty
        Object currentFaculty = getItem(position);


        TextView facultyName = listItemView.findViewById(R.id.faculty_name);
        facultyName.setText(currentFaculty.getFacultyName());

        TextView rank = listItemView.findViewById(R.id.rank);
        rank.setText(currentFaculty.getRank());

        TextView contact = listItemView.findViewById(R.id.contact);
        contact.setText(currentFaculty.getContactFaculty());

        // Find the TextView with the ID
        TextView email = listItemView.findViewById(R.id.email);
        email.setText(currentFaculty.getEmail());

        ImageView imageView = listItemView.findViewById(R.id.image);
        imageView.setImageResource(currentFaculty.getImage());

        return listItemView;
    }
}
