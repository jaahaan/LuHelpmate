package com.example.luhelpmate.UserAdapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Admin.CrEditActivity;
import com.example.luhelpmate.Data.CrData;
import com.example.luhelpmate.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrAdapterUser extends RecyclerView.Adapter<CrAdapterUser.ViewHolder> {

    private ArrayList<CrData> list;
    private final Context context;

    public CrAdapterUser(ArrayList<CrData> list, Context context) {
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

        holder.txtOption.setVisibility(View.GONE);

    }

        /**try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
        }

        holder.updateInfo.setOnClickListener(v -> {
        });*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_item_layout, parent, false);
        return new CrAdapterUser.ViewHolder(view);
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
