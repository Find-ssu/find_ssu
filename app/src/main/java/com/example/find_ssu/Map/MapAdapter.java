package com.example.find_ssu.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.Find.FindPost;
import com.example.find_ssu.R;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {

    Context mContext;
    static ArrayList<FindPost> items = new ArrayList<>();
    private static OnItemClickListener onItemClickListener;

    public MapAdapter(Context mContext, ArrayList<FindPost> items){
        this.mContext = mContext;
        this.items = items;
    }
    @NonNull
    @Override
    public MapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.map_info_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MapAdapter.ViewHolder holder, int position) {
        FindPost item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FindPost item){

        items.add(item);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(FindPost findPost);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView map_item_iv;
        TextView map_item_name_tv;
        TextView map_item_date_input_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            map_item_iv = itemView.findViewById(R.id.map_item_iv);
            map_item_name_tv = itemView.findViewById(R.id.map_item_name_tv);
            map_item_date_input_tv = itemView.findViewById(R.id.map_item_date_input_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }



        public void setItem(FindPost item) {
            map_item_name_tv.setText(item.getName());
            map_item_date_input_tv.setText(item.getDate());
            String imageUrl = item.getImage();
            if(imageUrl!=null){
                Glide.with(itemView.getContext()).load(imageUrl).into(map_item_iv);}
        }
    }
}
