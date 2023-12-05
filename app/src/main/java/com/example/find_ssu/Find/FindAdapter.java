package com.example.find_ssu.Find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.R;

import java.util.ArrayList;

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder> {

    Context context;
    static ArrayList<FindPost> items = new ArrayList<>();
    private static FindAdapter.OnItemClickListener onItemClickListener;

    public FindAdapter(Context context, ArrayList<FindPost> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FindAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemview, parent, false);
        return new FindAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindAdapter.ViewHolder holder, int position) {
        FindPost item = items.get(position);
        holder.setItem(item);

    }

    public void updateList(ArrayList<FindPost> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FindPost item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(FindPost findPost);
    }

    public void setOnItemClickListener(FindAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_iv;
        TextView item_name_tv;
        TextView item_date_input_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_iv = itemView.findViewById(R.id.item_iv);
            item_name_tv = itemView.findViewById(R.id.item_name_tv);
            item_date_input_tv = itemView.findViewById(R.id.item_data_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void setItem(FindPost item) {
            item_name_tv.setText(item.getName());
            item_date_input_tv.setText(item.getDate());
            String imageUrl = item.getImage();
            if (imageUrl != null) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .into(item_iv);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}

