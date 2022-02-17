package com.example.luhelpmate.AdminAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.Data.QuestionData;
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

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private ArrayList<QuestionData> list;
    private final Context context;

    public QuestionAdapter(ArrayList<QuestionData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView exam, session, code, title, initial, txtOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            exam = itemView.findViewById(R.id.exam);
            session = itemView.findViewById(R.id.session);
            code = itemView.findViewById(R.id.code);
            title = itemView.findViewById(R.id.title);
            initial = itemView.findViewById(R.id.initial);
            txtOption = itemView.findViewById(R.id.txtOption);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        QuestionData model = list.get(position);
        holder.exam.setText(model.getExam());
        holder.session.setText(model.getSession());
        holder.code.setText(model.getCode());
        holder.title.setText(model.getTitle());
        holder.initial.setText(model.getInitial());
        holder.txtOption.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).getImage().equals("")) {
                    Intent intent = new Intent(context, ImageViewActivity.class);
                    intent.putExtra("image", model.getImage());
                    intent.putExtra("title", model.getTitle());
                    holder.title.getContext().startActivity(intent);
                } else if (!list.get(position).getPdf().equals("")) {
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("pdf", list.get(position).getPdf());
                    holder.itemView.getContext().startActivity(intent);
                } else {
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
                        holder.txtOption.setVisibility(View.VISIBLE);
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
                                        } else if (!list.get(position).getImage().equals("")) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse(list.get(position).getImage()));
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.exam.getContext());
                                        builder.setTitle("Are You Sure?");
                                        builder.setPositiveButton("Delete", (dialog, which) -> {
                                            FirebaseDatabase.getInstance().getReference().child("Questions").child(model.getKey()).removeValue();
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
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<QuestionData> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
