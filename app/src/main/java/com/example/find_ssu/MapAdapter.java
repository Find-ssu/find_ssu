package com.example.find_ssu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FindPost> items = new ArrayList<>();

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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView map_item_iv;
        TextView map_item_name_tv;
        TextView map_item_date_input_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            map_item_iv = itemView.findViewById(R.id.map_item_iv);
            map_item_name_tv = itemView.findViewById(R.id.map_item_name_tv);
            map_item_date_input_tv = itemView.findViewById(R.id.map_item_date_input_tv);
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
