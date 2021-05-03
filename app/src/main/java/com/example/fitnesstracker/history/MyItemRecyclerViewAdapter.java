package com.example.fitnesstracker.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.RunItem;

import java.util.ArrayList;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    OnItemClick mOnItemClick;
    private List<RunItem> items;

    public MyItemRecyclerViewAdapter(Context context, OnItemClick mOnItemClick) {
        items = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        this.mOnItemClick = mOnItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.fragment_item, parent, false);
        return new MyViewHolder(view, mOnItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(items.get(position));

    }

    public void setValues(List<RunItem> listOfItem) {
        items.clear();
        if (listOfItem != null) {
            items.addAll(listOfItem);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameOfRunTextView;
        TextView distanceTextView;
        TextView speedTextView;
        TextView durationTextView;
        TextView dateTextView;
        TextView timeTextView;
        ImageView imageView;
        OnItemClick onItemClick;

        public void bind(RunItem runItem) {
            nameOfRunTextView.setText(runItem.getNameOfRun());
            distanceTextView.setText( String.valueOf(runItem.getDistance()+ " km"));
            speedTextView.setText(String.valueOf(runItem.getSpeed() + " km/h"));
            durationTextView.setText(String.valueOf(runItem.getMillis()+ " s"));
            dateTextView.setText(runItem.getDate());
            timeTextView.setText(runItem.getTime());
            Glide.with(imageView.getContext())
                    .load(runItem.getImageMap())
                    .override(140, 120)
                    .centerCrop()
                    .into(imageView);
        }

        public MyViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            nameOfRunTextView = itemView.findViewById(R.id.nameOfRun);
            distanceTextView = itemView.findViewById(R.id.distance);
            speedTextView = itemView.findViewById(R.id.speed);
            durationTextView = itemView.findViewById(R.id.duration);
            imageView = itemView.findViewById(R.id.image);
            dateTextView = itemView.findViewById(R.id.date);
            timeTextView = itemView.findViewById(R.id.time);

            this.onItemClick = onItemClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(getAdapterPosition());
        }

    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

}