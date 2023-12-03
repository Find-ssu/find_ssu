package com.example.find_ssu.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_ssu.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String TAG = "Chat";
    Context context;
    static ArrayList<ChatItem> items = new ArrayList<>();

    public ChatAdapter(Context context, ArrayList<ChatItem> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.chating_item, parent, false);
        return new ChatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView chat_sender;
        TextView chating_content;
        TextView chating_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chat_sender = itemView.findViewById(R.id.chat_sender);
            chating_content = itemView.findViewById(R.id.chating_content);
            chating_time = itemView.findViewById(R.id.chating_time);

        }

        public void setItem(ChatItem item){
            if(item.getDocumentId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                chat_sender.setText("보낸 쪽지");
                chat_sender.setTextColor(Color.parseColor("#000000"));
            }else {
                chat_sender.setText("받은 쪽지");
                chat_sender.setTextColor(Color.parseColor("#02A6CB"));
            }
            chating_content.setText(item.getMessage());
            chating_time.setText(item.getTimestamp());

        }
    }

}

