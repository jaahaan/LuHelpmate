package com.example.luhelpmate.UserAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.Data.BookData;
import com.example.luhelpmate.PdfViewerActivity;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookAdapterUser extends RecyclerView.Adapter<BookAdapterUser.ViewHolder> {

    private ArrayList<BookData> list;
    private final Context context;

    public BookAdapterUser(ArrayList<BookData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, author, edition, code, txtOption;
        ImageView download;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.author_name);
            edition = itemView.findViewById(R.id.edition);
            code = itemView.findViewById(R.id.code);
            download = itemView.findViewById(R.id.downloadIcon);
            txtOption = itemView.findViewById(R.id.txtOption);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_item, parent, false);
        return new BookAdapterUser.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        BookData model = list.get(position);
        holder.name.setText(model.getBookName());
        holder.author.setText(model.getAuthor());
        holder.edition.setText(model.getEdition());
        holder.code.setText(model.getCode());
        if (!list.get(position).getPdf().equals("")) {
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(list.get(position).getPdf()));
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        } else {
            holder.download.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getPdf().equals("")) {

                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("pdf", list.get(position).getPdf());
                    holder.itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Book Pdf Unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.txtOption.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<BookData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
