package com.example.luhelpmate.Notice;

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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.ImageViewActivity;
import com.example.luhelpmate.PdfViewerActivity;
import com.example.luhelpmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private ArrayList<NoticeData> list;
    private final Context context;

    public NoticeAdapter(ArrayList<NoticeData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, date, time;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.nDate);
            title = itemView.findViewById(R.id.nTitle);
            time = itemView.findViewById(R.id.nTime);
            image = itemView.findViewById(R.id.noticeImg);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        return new NoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NoticeData model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText(model.getTime());
        holder.date.setText(model.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getImage().equals("")) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("image", model.getImage());
                    intent.putExtra("title", model.getTitle());
                    holder.title.getContext().startActivity(intent);
                }
                if (!list.get(position).getPdf().equals("")) {
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("pdf", list.get(position).getPdf());
                    holder.itemView.getContext().startActivity(intent);
                } if (list.get(position).getImage().equals("") && list.get(position).getPdf().equals("")) {
                    Toast.makeText(holder.itemView.getContext(), "Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    if (value.getString("admin").equals("1")) {
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                                popupMenu.inflate(R.menu.book_menu);
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(item -> {
                                    if (item.getItemId() == R.id.download) {
                                        if (!list.get(position).getPdf().equals("")) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(list.get(position).getPdf()));
                                            holder.itemView.getContext().startActivity(intent);
                                        } else if (!list.get(position).getImage().equals("")) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(list.get(position).getImage()));
                                            holder.itemView.getContext().startActivity(intent);
                                        } else {
                                            Toast.makeText(holder.itemView.getContext(), "Not Available", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else if (item.getItemId() == R.id.edit) {
                                        Intent intent = new Intent(holder.itemView.getContext(), EditNotice.class);
                                        intent.putExtra("title", model.getTitle());
                                        intent.putExtra("pdf", model.getPdf());
                                        intent.putExtra("image", model.getImage());
                                        intent.putExtra("key", model.getKey());
                                        holder.itemView.getContext().startActivity(intent);
                                    } else if (item.getItemId() == R.id.delete) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.title.getContext());
                                        builder.setTitle("Delete Notice?");
                                        builder.setMessage("Title: "+ holder.title.getText().toString());
                                        builder.setPositiveButton("Delete", (dialog, which) -> {
                                            FirebaseDatabase.getInstance().getReference().child("Notice").child(model.getKey()).removeValue();
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        });
                                        builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                                        builder.show();
                                    }
                                    return false;
                                });

                                return true;
                            }
                        });

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<NoticeData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
