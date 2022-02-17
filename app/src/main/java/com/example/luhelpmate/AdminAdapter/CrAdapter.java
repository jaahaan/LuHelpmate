package com.example.luhelpmate.AdminAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Admin.CrEditActivity;
import com.example.luhelpmate.Admin.FacultyEditActivity;
import com.example.luhelpmate.Data.CrData;
import com.example.luhelpmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrAdapter extends RecyclerView.Adapter<CrAdapter.ViewHolder> {

    private ArrayList<CrData> list;
    private final Context context;

    public CrAdapter(ArrayList<CrData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView crBatch,b, s, crSection, name, phone, email, txtOption;
        ImageView image;
        LinearLayout callIcon, emailIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            b = itemView.findViewById(R.id.batch);
            s = itemView.findViewById(R.id.section);

            crBatch = itemView.findViewById(R.id.crBatch);
            crSection = itemView.findViewById(R.id.crSection);
            name = itemView.findViewById(R.id.crName);
            phone = itemView.findViewById(R.id.crContact);
            callIcon = itemView.findViewById(R.id.callIcon);
            email = itemView.findViewById(R.id.crEmail);
            emailIcon = itemView.findViewById(R.id.emailIcon);
            txtOption = itemView.findViewById(R.id.txtOption);

            image= itemView.findViewById(R.id.crImage);
            //Glide.with(crImage.getContext()).load(item.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(crImage);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CrData model = list.get(position);
        holder.b.setText("Batch: ");
        holder.s.setText("Section: ");

        holder.crBatch.setText(model.getBatch());
        holder.crSection.setText(model.getSection());
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.email.setText(model.getEmail());
        Glide.with(holder.image).load(model.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(holder.image);

        holder.txtOption.setVisibility(View.GONE);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null && value.exists()){
                    if (value.getString("admin").equals("1")){
                        holder.txtOption.setVisibility(View.VISIBLE);
                        holder.txtOption.setOnClickListener(v -> {
                            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                            popupMenu.inflate(R.menu.faculty_menu);
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.editFaculty:
                                        Intent intent = new Intent(holder.txtOption.getContext(), CrEditActivity.class);

                                        intent.putExtra("batch", model.getBatch());
                                        intent.putExtra("section", model.getSection());
                                        intent.putExtra("name", model.getName());
                                        intent.putExtra("id", model.getId());
                                        intent.putExtra("phone", model.getPhone());
                                        intent.putExtra("email", model.getEmail());
                                        intent.putExtra("image", model.getImage());
                                        intent.putExtra("key", model.getKey());
                                        holder.txtOption.getContext().startActivity(intent);
                                        break;

                                    case R.id.delete:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                                        builder.setTitle("Are You Sure?");
                                        builder.setPositiveButton("Delete", (dialog, which) -> {
                                            FirebaseDatabase.getInstance().getReference().child("Cr Info").child(model.getKey()).removeValue();
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


        holder.callIcon.setOnClickListener(v -> {
            Pattern p = Pattern.compile("[0][1][0-9]{9}");
            Matcher m = p.matcher(model.getPhone());
            if (m.find()) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + m.group()));
                holder.phone.getContext().startActivity(dialIntent);
            }
        });

        holder.emailIcon.setOnClickListener(v -> {
            Matcher e = Patterns.EMAIL_ADDRESS.matcher(model.getEmail());
            if (e.find()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + e.group())); // only email apps should handle this
                holder.email.getContext().startActivity(Intent.createChooser(emailIntent, "Complete action using"));
            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_item_layout, parent, false);
        return new CrAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<CrData> newlist){
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }
}
