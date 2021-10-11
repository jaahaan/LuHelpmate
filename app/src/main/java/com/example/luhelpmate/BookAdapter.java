package com.example.luhelpmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;


public class BookAdapter extends ArrayAdapter<Object> {

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app
     * @param objects is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Object> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_books, parent, false);
        }

        // Find the book at the given position in the list of books
        Object currentBook = getItem(position);


        // Find the TextView in the activity_books.xml layout with the ID
        TextView bookName = listItemView.findViewById(R.id.book_name);

        bookName.setText(currentBook.getBookName());

        // Find the TextView in the activity_books.xml layout with the ID
        TextView authorName = listItemView.findViewById(R.id.author_name);

        authorName.setText(currentBook.getAuthorName());

        // Find the TextView in the activity_books.xml layout with the ID
        TextView edition = listItemView.findViewById(R.id.edition);

        edition.setText(currentBook.getEdition());


        // Find the TextView in the activity_books.xml layout with the ID
        TextView courseCode = listItemView.findViewById(R.id.code);

        courseCode.setText(currentBook.getCourseCode());

        return listItemView;
    }
}
