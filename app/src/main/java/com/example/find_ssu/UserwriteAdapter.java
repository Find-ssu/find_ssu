package com.example.find_ssu;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.ItemviewUserWriteBinding;

import java.util.List;

public class

UserwriteAdapter extends RecyclerView.Adapter<UserwriteAdapter.ViewHolder> {
    private List<FindPost> dataList;


    public UserwriteAdapter(List<FindPost> dataList) {this.dataList = dataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_user_write, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FindPost data = dataList.get(position);
        // 데이터를 화면에 표시하기 위한 작업 수행
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_write_iv;
        TextView user_write_tv;
        TextView user_write_date_tv;
        // ViewHolder 클래스 정의
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 초기화 작업 수행
            user_write_iv=itemView.findViewById(R.id.item_user_write_iv);
            user_write_tv=itemView.findViewById(R.id.item_user_write_name_tv);
            user_write_date_tv=itemView.findViewById(R.id.item_user_write_date_input_tv);
        }

        public void bind(FindPost data) {
            // 데이터를 화면의 뷰에 바인딩하는 작업 수행
            user_write_tv.setText(data.getName());
            user_write_date_tv.setText(data.getDate());
            // 예시 URL 문자열
            String imageUrl = data.getImage();
            if(imageUrl!=null){
                Glide.with(itemView.getContext()).load(imageUrl).into(user_write_iv);}

        }
    }
}
