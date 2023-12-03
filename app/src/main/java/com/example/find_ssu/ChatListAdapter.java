package com.example.find_ssu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    Context context;
    static ArrayList<ChatItem> items = new ArrayList<>();
    private static ChatListAdapter.OnItemClickListener onItemClickListener;

    public ChatListAdapter(Context context, ArrayList<ChatItem> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.chat_list_item, parent, false);
        return new ChatListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        ChatItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ChatItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(ChatItem chatItem);
    }

    public void setOnItemClickListener(ChatListAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView chating_content;
        TextView chat_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chating_content = itemView.findViewById(R.id.chating_content);
            chat_time = itemView.findViewById(R.id.chat_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        public void setItem(ChatItem item){
            chating_content.setText(item.getMessage());
            chat_time.setText(item.getTimestamp());

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
