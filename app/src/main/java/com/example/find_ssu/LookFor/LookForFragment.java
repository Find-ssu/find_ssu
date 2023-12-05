package com.example.find_ssu.LookFor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.find_ssu.Find.FindPost;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.FragmentLookForBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class LookForFragment extends Fragment {
    private ArrayList<LookForPost> list = new ArrayList<>();
    private ArrayList<LookForPost> originalList = new ArrayList<>(); // 원본 데이터 보존
    private LookForAdapter adapter;
    private static final String TAG = "FINDSSU";
    private FirebaseFirestore db;
    private FragmentLookForBinding lookForBinding;

    private RecyclerView look_for_rv_view;
    private EditText look_for_search_word_et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        adapter = new LookForAdapter(requireContext(), list);

        adapter.setOnItemClickListener(new LookForAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LookForPost lookForPost) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_navi, LookForClickFragment.newInstance(lookForPost));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lookForBinding = FragmentLookForBinding.inflate(inflater,container,false);
        View rootview = lookForBinding.getRoot();

        look_for_rv_view = lookForBinding.lookForRv;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        look_for_rv_view.setLayoutManager(layoutManager);
        look_for_rv_view.setAdapter(adapter);

        look_for_search_word_et = lookForBinding.lookForSearchWordEt;

        //글쓰기버튼 클릭이벤트
        lookForBinding.lookForFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환
                Intent intent = new Intent(requireContext(), LookForFabClickActivity.class);
                startActivity(intent);
            }
        });

        look_for_search_word_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 검색어가 변경될 때마다 호출되는 부분
                String searchText = charSequence.toString().trim();
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return rootview;
    }

    private void filterData(String searchText) {

        if (originalList.isEmpty()) {
            originalList.addAll(list); // 원본 데이터를 초기화
        }

        ArrayList<LookForPost> filteredList = new ArrayList<>();

        if (searchText.isEmpty()) {
            filteredList.addAll(originalList); // 검색어가 비어있을 때 원본 데이터 보여주기
        } else {
            // 검색어로 필터링된 결과를 리스트에 추가
            for (LookForPost post : originalList) {
                if (post.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(post);
                }
            }
        }

        adapter.updateList(filteredList); // 필터링된 결과를 어댑터에 전달하여 업데이트
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadDataFromFirestore();

    }

    private void loadDataFromFirestore() {
        db.collection("LookForPost")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String date = document.getString("date");
                            String more = document.getString("more");
                            String img = document.getString("image");
                            String documentuid = document.getString("documentuid");

                            LookForPost lookForPost = new LookForPost(name, location, date, more, img, documentuid);
                            adapter.addItem(lookForPost);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

}
