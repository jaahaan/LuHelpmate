package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Object> {

    public CourseAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_course_offerings, parent, false);
        }

        // Find the course at the given position in the list
        Object currentCourse = getItem(position);

        TextView code = listItemView.findViewById(R.id.cCode);
        code.setText(currentCourse.getBatch());

        TextView title = listItemView.findViewById(R.id.cTitle);
        title.setText(currentCourse.getCrName());

        TextView credit = listItemView.findViewById(R.id.credit);
        credit.setText(currentCourse.getId());

        TextView prerequisite = listItemView.findViewById(R.id.prerequisite);
        prerequisite.setText(currentCourse.getContact());

        return listItemView;
    }
}
