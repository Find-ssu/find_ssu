package com.example.find_ssu.LookFor;

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

public class LookForAdapter extends RecyclerView.Adapter<LookForAdapter.ViewHolder> {

    Context context;
    static ArrayList<LookForPost> items = new ArrayList<>();
    private static LookForAdapter.OnItemClickListener onItemClickListener;

    public LookForAdapter(Context context, ArrayList<LookForPost> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public LookForAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemview, parent, false);
        return new LookForAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LookForAdapter.ViewHolder holder, int position) {
        LookForPost item = items.get(position);
        holder.setItem(item);

    }

    public void updateList(ArrayList<LookForPost> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LookForPost item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(LookForPost lookForPost);
    }

    public void setOnItemClickListener(LookForAdapter.OnItemClickListener listener) {
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

        public void setItem (LookForPost item){
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

