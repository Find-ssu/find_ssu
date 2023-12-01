package com.example.find_ssu;

import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.ItemviewUserWriteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserwriteAdapter<T> extends RecyclerView.Adapter<UserwriteAdapter.ViewHolder> {
    private List<T> dataList;
    public static Context context;
    private static FirebaseFirestore db;



    public UserwriteAdapter(List<T> dataList, Context context){
        this.dataList = dataList;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_user_write, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        T data = dataList.get(position);
        if (data instanceof FindPost) {
            holder.bind((FindPost) data);
            holder.model=(FindPost)data;
        } else if (data instanceof LookForPost) {
            holder.bind((LookForPost) data);
            holder.model=(LookForPost)data;
        }

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        private T model;
        ImageView user_write_iv;
        TextView user_write_tv;
        TextView user_write_date_tv;
        ImageView user_write_del_btn;
        String document_uid;
        // ViewHolder 클래스 정의
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 초기화 작업 수행
            user_write_iv=itemView.findViewById(R.id.item_user_write_iv);
            user_write_tv=itemView.findViewById(R.id.item_user_write_name_tv);
            user_write_date_tv=itemView.findViewById(R.id.item_user_write_date_input_tv);
            user_write_del_btn=itemView.findViewById(R.id.item_user_write_del_btn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //프레그먼트 전환
                    // 전환할 프래그먼트 인스턴스 생성
                    FragmentManager fragmentManager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if(model instanceof FindPost)
                        fragmentTransaction.replace(R.id.activity_user_write, FindClickFragment.newInstance((FindPost) model));
                    else if (model instanceof LookForPost)
                        fragmentTransaction.replace(R.id.activity_user_write, LookForClickFragment.newInstance((LookForPost) model));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            user_write_del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity)view.getContext());
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    View dialogView = inflater.inflate(R.layout.delete_dialog, null);
                    Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_btn);
                    Button okayButton = dialogView.findViewById(R.id.dialog_okay_btn);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    okayButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //파이어베이스 삭제
                            db = FirebaseFirestore.getInstance();
                            db.collection("FindPost").document(document_uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "게시물 삭제 완료", Toast.LENGTH_SHORT).show();
                                    if (context instanceof Activity) {
                                        ((Activity) context).finish();
                                    }

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "게시물 삭제 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            db.collection("LookForPost").document(document_uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "게시물 삭제 완료", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "게시물 삭제 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                            dialog.dismiss();
                            Intent intent = new Intent((AppCompatActivity)view.getContext(), UserWriteActivity.class);
                            context.startActivity(intent);}
                    });

                    dialog.show();
                }
            });

        }

        public void bind(FindPost data) {
                // 데이터를 화면의 뷰에 바인딩하는 작업 수행
                user_write_tv.setText(data.getName());
                user_write_date_tv.setText(data.getDate());
                document_uid= data.getDocumentuid();
                // 예시 URL 문자열
                String imageUrl = data.getImage();
                if (imageUrl != null) {
                    Glide.with(itemView.getContext()).load(imageUrl).into(user_write_iv);
                }
        }
        public void bind(LookForPost data) {
            // 데이터를 화면의 뷰에 바인딩하는 작업 수행
            user_write_tv.setText(data.getName());
            user_write_date_tv.setText(data.getDate());
            document_uid= data.getDocumentuid();
            // 예시 URL 문자열
            String imageUrl = data.getImage();
            if (imageUrl != null) {
                Glide.with(itemView.getContext()).load(imageUrl).into(user_write_iv);
            }
        }
    }
}
