package com.example.find_ssu.Home;

import android.os.Bundle;

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
import com.example.find_ssu.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<HomePost> list = new ArrayList<>();
    private ArrayList<HomePost> originalList = new ArrayList<>(); // 원본 데이터 보존
    private HomeAdapter adapter;
    private FirebaseFirestore db;
    private FragmentHomeBinding binding;
    private RecyclerView home_rv_view;
    private static final String TAG = "FINDSSU";
    private EditText home_search_word_et;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        adapter = new HomeAdapter(requireContext(), list);

        adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HomePost homePost) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_navi, HomeClickFragment.newInstance(homePost));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        home_rv_view = binding.homeRv;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        home_rv_view.setLayoutManager(layoutManager);
        home_rv_view.setAdapter(adapter);

        home_search_word_et = binding.homeSearchWordEt;
        home_search_word_et.addTextChangedListener(new TextWatcher() {
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


        return rootView;
    }

    private void filterData(String searchText) {

        if (originalList.isEmpty()) {
            originalList.addAll(list); // 원본 데이터를 초기화
        }

        ArrayList<HomePost> filteredList = new ArrayList<>();

        if (searchText.isEmpty()) {
            filteredList.addAll(originalList); // 검색어가 비어있을 때 원본 데이터 보여주기
        } else {
            // 검색어로 필터링된 결과를 리스트에 추가
            for (HomePost post : originalList) {
                if (post.gethomeName().toLowerCase().contains(searchText.toLowerCase())) {
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
        db.collection("InstagramData")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String location = document.getString("location");
                            String number = document.getString("number");
                            String date = document.getString("date");
                            String img = document.getString("img");
                            Number time = (Number) document.get("timestamp");
                            String timestamp = String.valueOf(time);

                            HomePost homePost = new HomePost(number, name, location, date, img, timestamp);
                            adapter.addItem(homePost);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
