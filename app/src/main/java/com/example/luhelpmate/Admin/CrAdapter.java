package com.example.luhelpmate.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Data.CrData;
import com.example.luhelpmate.Object;
import com.example.luhelpmate.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrAdapter extends ArrayAdapter<CrData> {



    public CrAdapter(Context context, List<CrData> crData) {
        super(context, 0, crData);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.cr_item_layout, parent, false);
        }

        // Find the cr at the given position in the list
        CrData item = getItem(position);

        TextView crBatch = listItemView.findViewById(R.id.crBatch);
        crBatch.setText(item.getBatch());

        TextView crSection = listItemView.findViewById(R.id.crSection);
        crSection.setText(item.getSection());

        TextView crName = listItemView.findViewById(R.id.crName);
        crName.setText(item.getName());

        TextView crId = listItemView.findViewById(R.id.crId);
        crId.setText(item.getId());

        TextView crContact = listItemView.findViewById(R.id.crContact);
        crContact.setText(item.getPhone());

        TextView crEmail = listItemView.findViewById(R.id.crEmail);
        crEmail.setText(item.getEmail());

        ImageView crImage = listItemView.findViewById(R.id.crImage);
        Glide.with(crImage.getContext()).load(item.getImage()).circleCrop().placeholder(R.drawable.download).error(R.drawable.download).into(crImage);

        return listItemView;

        /**try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        } catch (Exception e) {
        }

        holder.updateInfo.setOnClickListener(v -> {
        });*/
    }


}
