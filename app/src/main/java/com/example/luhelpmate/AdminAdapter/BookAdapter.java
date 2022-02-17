package com.example.luhelpmate.AdminAdapter;

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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private ArrayList<BookData> list;
    private final Context context;

    public BookAdapter(ArrayList<BookData> list, Context context) {
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
        return new BookAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        BookData model = list.get(position);
        holder.name.setText(model.getBookName());
        holder.author.setText(model.getAuthor());
        holder.edition.setText(model.getEdition());
        holder.code.setText(model.getCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getPdf().equals("")) {
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("pdf", list.get(position).getPdf());
                    holder.itemView.getContext().startActivity(intent);
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.download.setVisibility(View.GONE);

        holder.txtOption.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
            popupMenu.inflate(R.menu.book_menu);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.download:
                        if (!list.get(position).getPdf().equals("")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(list.get(position).getPdf()));
                            holder.itemView.getContext().startActivity(intent);
                        } else {
                            Toast.makeText(holder.itemView.getContext(), "Not Available", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.edit:
                        /*Intent intent = new Intent(holder.txtOption.getContext(), FacultyEditActivity.class);

                        intent.putExtra("bookName", model.getBookName());
                        intent.putExtra("author", model.getAuthor());
                        intent.putExtra("edition", model.getEdition());
                        intent.putExtra("code", model.getCode());
                        intent.putExtra("pdf", model.getPdf());
                        intent.putExtra("key", model.getKey());
                        holder.txtOption.getContext().startActivity(intent);*/
                        break;

                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                        builder.setTitle("Are You Sure?");
                        builder.setPositiveButton("Delete", (dialog, which) -> {
                            FirebaseDatabase.getInstance().getReference().child("Books").child(model.getKey()).removeValue();
                            //FirebaseStorage.getInstance().getReference("Faculty").child(model.getImage()).delete();
                            Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                        builder.show();
                        break;
                }
                return false;
            });
        });

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
