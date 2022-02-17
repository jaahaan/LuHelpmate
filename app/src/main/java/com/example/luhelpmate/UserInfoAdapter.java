package com.example.luhelpmate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Admin.FacultyEditActivity;
import com.example.luhelpmate.Admin.HomeActivityAdmin;
import com.example.luhelpmate.Data.NoticeData;
import com.example.luhelpmate.Data.User;
import com.example.luhelpmate.User.HomeActivityUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {

    Context context;
    ArrayList<User> list;

    public UserInfoAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);

        holder.name.setText(user.getName());
        holder.initial.setText(user.getInitial());
        holder.designation.setText(user.getDesignation());
        holder.email.setText(user.getEmail());
        holder.admin.setVisibility(View.GONE);
        Glide.with(holder.image).load(user.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(holder.image);

        holder.emailIcon.setOnClickListener(v -> {
            Matcher e = Patterns.EMAIL_ADDRESS.matcher(user.getEmail());
            if (e.find()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + e.group())); // only email apps should handle this
                holder.email.getContext().startActivity(Intent.createChooser(emailIntent, "Complete action using"));
            }
        });

        DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("admin") != null) {
                    if (documentSnapshot.getString("admin").equals("1")) {
                        holder.admin.setVisibility(View.VISIBLE);
                        holder.txtOption.setOnClickListener(v -> {
                            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                            popupMenu.inflate(R.menu.user_menu2);
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.removeAdmin:
                                        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("email", user.getEmail());
                                                userInfo.put("image", user.getImage());
                                                userInfo.put("name", user.getName());
                                                userInfo.put("designation", user.getDesignation());
                                                userInfo.put("initial", user.getInitial());
                                                userInfo.put("uid", user.getUid());
                                                userInfo.put("admin", "2");
                                                df.set(userInfo);
                                                holder.admin.setVisibility(View.GONE);
                                            }
                                        });
                                        break;
                                    case R.id.delete:
                                        break;
                                }
                                return false;
                            });
                        });
                    }
                    if (documentSnapshot.getString("admin").equals("2")) {
                        holder.admin.setVisibility(View.GONE);
                        holder.txtOption.setOnClickListener(v -> {
                            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
                            popupMenu.inflate(R.menu.user_menu);
                            popupMenu.show();
                            popupMenu.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.makeAdmin:
                                        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("email", user.getEmail());
                                                userInfo.put("image", user.getImage());
                                                userInfo.put("name", user.getName());
                                                userInfo.put("designation", user.getDesignation());
                                                userInfo.put("initial", user.getInitial());
                                                userInfo.put("uid", user.getUid());
                                                userInfo.put("admin", "1");
                                                df.set(userInfo);
                                                holder.admin.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        break;
                                    case R.id.delete:
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name, initial, designation, emailIcon, email, admin, txtOption;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            initial = itemView.findViewById(R.id.initial);
            designation = itemView.findViewById(R.id.designation);
            emailIcon = itemView.findViewById(R.id.emailIcon);
            email = itemView.findViewById(R.id.email);
            admin = itemView.findViewById(R.id.admin);
            txtOption = itemView.findViewById(R.id.txtOption);

        }
    }

    public void setFilter(ArrayList<User> newlist) {
        list = new ArrayList<>();
        list.addAll(newlist);
        notifyDataSetChanged();
    }

}
