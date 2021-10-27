package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoticeAdapter extends ArrayAdapter<Object> {
    public NoticeAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_notice, parent, false);
        }

        Object currentCr = getItem(position);

        TextView noticeDate = listItemView.findViewById(R.id.noticeDate);
        noticeDate.setText(currentCr.getNoticeDate());

        TextView noticeTitle = listItemView.findViewById(R.id.noticeTitle);
        noticeTitle.setText(currentCr.getNoticeTitle());

        return listItemView;
    }
}
