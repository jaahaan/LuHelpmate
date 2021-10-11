package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RoutineAdapter extends ArrayAdapter<Object> {

    public RoutineAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_routine, parent, false);
        }

        // Find the course at the given position in the list
        Object currentCourse = getItem(position);

        TextView time = listItemView.findViewById(R.id.time);
        time.setText(currentCourse.getBatch());

        TextView code = listItemView.findViewById(R.id.courseCode);
        code.setText(currentCourse.getCrName());

        TextView teacher = listItemView.findViewById(R.id.teacher);
        teacher.setText(currentCourse.getId());

        TextView room = listItemView.findViewById(R.id.room);
        room.setText(currentCourse.getContact());

        return listItemView;
    }
}
