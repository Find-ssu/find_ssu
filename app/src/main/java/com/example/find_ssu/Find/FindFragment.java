package com.example.find_ssu.Find;

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

import com.example.find_ssu.Map.MapsActivity;
import com.example.find_ssu.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FindFragment extends Fragment {
    private ArrayList<FindPost> list = new ArrayList<>();
    private ArrayList<FindPost> originalList = new ArrayList<>(); // 원본 데이터 보존
    private FindAdapter adapter;
    private static final String TAG = "FINDSSU";
    private FirebaseFirestore db;
    private FragmentFindBinding findBinding;

    private RecyclerView find_rv_view;
    private EditText find_search_word_et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        adapter = new FindAdapter(requireContext(), list);

        adapter.setOnItemClickListener(new FindAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FindPost findPost) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_navi, FindClickFragment.newInstance(findPost));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //find_search_word_et.setText(null);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        findBinding = FragmentFindBinding.inflate(inflater, container, false);
        View rootView = findBinding.getRoot();

        find_rv_view = findBinding.findRv;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        find_rv_view.setLayoutManager(layoutManager);
        find_rv_view.setAdapter(adapter);

        find_search_word_et = findBinding.findSearchWordEt;

        // 글쓰기 버튼 클릭 이벤트
        findBinding.findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환
                Intent intent = new Intent(requireContext(), FindFabClickActivity.class);
                startActivity(intent);

                //find_search_word_et.setText(null);
            }
        });

        find_search_word_et.addTextChangedListener(new TextWatcher() {
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

        //지도버튼 클릭이벤트
        findBinding.findMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);

                //find_search_word_et.setText(null);
            }
        });

        return rootView;
    }

    private void filterData(String searchText) {

        if (originalList.isEmpty()) {
            originalList.addAll(list); // 원본 데이터를 초기화
        }

        ArrayList<FindPost> filteredList = new ArrayList<>();

        if (searchText.isEmpty()) {
            filteredList.addAll(originalList); // 검색어가 비어있을 때 원본 데이터 보여주기
        } else {
            // 검색어로 필터링된 결과를 리스트에 추가
            for (FindPost post : originalList) {
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
        db.collection("FindPost")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (QueryDocumentSnapshot document : value) {
                                String name = document.getString("name");
                                String location = document.getString("location");
                                String location_detail = document.getString("location_detail");
                                String date = document.getString("date");
                                String more = document.getString("more");
                                String image = document.getString("image");
                                String documentuid = document.getString("documentuid");
                                String uid = document.getString("uid");

                                FindPost findPost = new FindPost(name, location, location_detail, date, more, image, documentuid, uid);
                                adapter.addItem(findPost);
                            }
                            adapter.notifyDataSetChanged();
                    }
                });
    }
}
