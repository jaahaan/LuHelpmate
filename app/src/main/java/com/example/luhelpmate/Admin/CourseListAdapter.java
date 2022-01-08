package com.example.luhelpmate.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.luhelpmate.Data.CourseData;
import com.example.luhelpmate.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class CourseListAdapter extends FirebaseRecyclerAdapter<CourseData, CourseListAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView code, title, credit, prerequisite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.cCode);
            title = itemView.findViewById(R.id.cTitle);
            credit = itemView.findViewById(R.id.cCredit);
            prerequisite = itemView.findViewById(R.id.cPrerequisite);
        }
    }

    public CourseListAdapter(FirebaseRecyclerOptions<CourseData> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courselist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    /**public void filterList(ArrayList<CourseData> filterList){
        list =filterList;
        notifyDataSetChanged();
    }
*/
    @Override
    protected void onBindViewHolder(@NonNull CourseListAdapter.ViewHolder holder, int position, @NonNull CourseData model) {
            holder.code.setText(model.getCode());
            holder.title.setText(model.getTitle());
            holder.credit.setText(model.getCredit());
            holder.prerequisite.setText(model.getPrerequisite());
        }


}
/*holder.d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.batch.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete..?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Batch List").child(getRef(position).getKey()).removeValue();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });*/