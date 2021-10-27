package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CrAdapter extends ArrayAdapter<Object> {
    public CrAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_cr, parent, false);
        }

        // Find the cr at the given position in the list of books
        Object currentCr = getItem(position);

        TextView batch = listItemView.findViewById(R.id.batch);
        batch.setText(currentCr.getBatch());

        TextView crName = listItemView.findViewById(R.id.cr_name);
        crName.setText(currentCr.getCrName());

        TextView crId = listItemView.findViewById(R.id.crId);
        crId.setText(currentCr.getId());

        TextView contact = listItemView.findViewById(R.id.crPhoneNo);
        contact.setText(currentCr.getContact());

        return listItemView;
    }
}
