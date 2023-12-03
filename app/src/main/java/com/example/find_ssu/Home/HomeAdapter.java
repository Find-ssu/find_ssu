package com.example.find_ssu.Home;

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

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    static ArrayList<HomePost> items = new ArrayList<>();
    private static HomeAdapter.OnItemClickListener onItemClickListener;

    public HomeAdapter(Context context, ArrayList<HomePost> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.itemview, parent, false);
        return new HomeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        HomePost item = items.get(position);
        holder.setItem(item);

    }

    public void updateList(ArrayList<HomePost> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(HomePost item){
        items.add(item);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(HomePost homePost);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView item_iv;
        TextView item_name_tv;
        TextView item_date_input_tv;

        public ViewHolder(@NonNull View itemView){
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

        public void setItem(HomePost item){
            item_name_tv.setText(item.gethomeName());
            item_date_input_tv.setText(item.gethomeDate());
            String imageUrl = item.getImg();
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
