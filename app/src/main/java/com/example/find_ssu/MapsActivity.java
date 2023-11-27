package com.example.find_ssu;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.find_ssu.databinding.ActivityMapsBinding;
import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public ArrayList<FindPost> list = new ArrayList<>();
    private FirebaseFirestore db;
    private static final String TAG = "MapsActivity";
    private MapAdapter adapter = new MapAdapter(this,list);;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;;
    LinearLayout mapInfoLayout;

    RecyclerView findpost_map_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // ImageButton 참조 가져오기
        ImageButton backButton = findViewById(R.id.map_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();
        mapInfoLayout = findViewById(R.id.map_info_layout);
        findpost_map_info = findViewById(R.id.map_info_rv);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //줌 경계 설정
        LatLngBounds bounds = new LatLngBounds(
                new LatLng(37.496369 - 0.003, 126.957415 - 0.001),
                new LatLng(37.496369 + 0.002, 126.957415 + 0.001)
        );
        googleMap.setLatLngBoundsForCameraTarget(bounds);

        //카메라 위치 설정
        LatLng soongsil = new LatLng(37.496369, 126.957415);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(soongsil, 16));

        //커스텀 버튼 불러오기
        View marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker, null);
        TextView tv_marker;
        tv_marker = marker_root_view.findViewById(R.id.map_marker);

        //법학관
        LatLng law_officer = new LatLng(37.496540, 126.954292);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(law_officer)
                .anchor(0.5f, 0.5f) // polyline이 마커의 하단이 아닌 중앙을 꼭짓점으로 하도록 수정
                .title("법학관");
        View marker = LayoutInflater.from(this).inflate(R.layout.marker, null);
        Marker law = mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view))));

        //백마상
        LatLng white_horse_statue = new LatLng(37.4966196, 126.957354);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1
                .position(white_horse_statue)
                .anchor(0.5f, 0.5f)
                .title("백마상");
        View marker1 = LayoutInflater.from(this).inflate(R.layout.marker, null);
        Marker whitehorese = mMap.addMarker(markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view))));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.equals(law)) {
                    adapter.items.clear();
                    getAllDocumentsInACollection("법학관");
                    mapInfoLayout.setVisibility(View.VISIBLE);

                } else if (marker.equals(whitehorese)) {
                    adapter.items.clear();
                    getAllDocumentsInACollection("백마상");
                    mapInfoLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapInfoLayout.setVisibility(View.GONE);
                adapter.items.clear();

            }
        });
    }

    private void getAllDocumentsInACollection(String building) {
        db.collection("FindPost")
                .whereEqualTo("location", building)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name=document.getString("name");
                                String location=document.getString("location");
                                String location_detail=document.getString("location_detail");
                                String date=document.getString("date");
                                String more=document.getString("more");
                                String image=document.getString("image");
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                FindPost findPost=new FindPost(name,location,location_detail, date, more, image);

                                findpost_map_info = findViewById(R.id.map_info_rv);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                findpost_map_info.setLayoutManager(layoutManager);

                                findpost_map_info.setAdapter(adapter);
                                adapter.addItem(findPost);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}