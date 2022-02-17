package com.example.luhelpmate.UserAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luhelpmate.ImageViewActivity;
import com.example.luhelpmate.Data.NoticeData;
import com.example.luhelpmate.R;

import java.util.ArrayList;

public class NoticeAdapterUser extends RecyclerView.Adapter<NoticeAdapterUser.ViewHolder> {

    private ArrayList<NoticeData> list;
    private final Context context;

    public NoticeAdapterUser(ArrayList<NoticeData> list, Context context) {
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
        return new NoticeAdapterUser.ViewHolder(view);
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
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("title", item.getTitle());
                holder.title.getContext().startActivity(intent);
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
