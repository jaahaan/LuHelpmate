package com.example.luhelpmate.Slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.luhelpmate.Faculty.FacultyData;
import com.example.luhelpmate.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    private ArrayList<SliderData> list;
    private Context context;

    public SliderAdapter(ArrayList<SliderData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SliderData model = list.get(position);
        Glide.with(holder.image).load(model.getImage()).placeholder(R.drawable.image_icon).error(R.drawable.image_icon).into(holder.image);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {

        ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.image_view);

        }
    }

}
