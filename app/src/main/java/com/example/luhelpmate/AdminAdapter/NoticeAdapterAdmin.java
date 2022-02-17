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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.ImageViewActivity;
import com.example.luhelpmate.Data.NoticeData;
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

public class NoticeAdapterAdmin extends RecyclerView.Adapter<NoticeAdapterAdmin.ViewHolder> {

    private ArrayList<NoticeData> list;
    private final Context context;

    public NoticeAdapterAdmin(ArrayList<NoticeData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, date, time;
        ImageView image, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.nDate);
            title = itemView.findViewById(R.id.nTitle);
            time = itemView.findViewById(R.id.nTime);
            image = itemView.findViewById(R.id.noticeImg);
            delete = itemView.findViewById(R.id.delete);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        return new NoticeAdapterAdmin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NoticeData item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.time.setText(item.getTime());
        holder.date.setText(item.getDate());
        holder.delete.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getImage().equals("")) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("title", item.getTitle());
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
                        holder.delete.setVisibility(View.VISIBLE);
                        holder.delete.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.title.getContext());
                            builder.setTitle("Are You Sure?");
                            builder.setPositiveButton("Delete", (dialog, which) -> {
                                FirebaseDatabase.getInstance().getReference().child("Notice").child(item.getKey()).removeValue();
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                            });
                            builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(context, "Cancelled.", Toast.LENGTH_SHORT).show());
                            builder.show();
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
