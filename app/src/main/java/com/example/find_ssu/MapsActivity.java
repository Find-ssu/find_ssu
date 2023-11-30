package com.example.find_ssu;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.google.android.gms.maps.CameraUpdate;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    public ArrayList<FindPost> list = new ArrayList<>();
    private FirebaseFirestore db;
    private static final String TAG = "MapsActivity";
    private MapAdapter adapter = new MapAdapter(this,list);

    private GoogleMap mMap;
    LinearLayout mapInfoLayout;
    RecyclerView findpost_map_info;
    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();
        mapInfoLayout = findViewById(R.id.map_info_layout);
        findpost_map_info = findViewById(R.id.map_info_rv);


        // ImageButton 참조 가져오기
        ImageButton backButton = findViewById(R.id.map_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                new LatLng(37.496369 - 0.005, 126.957415 - 0.005),
                new LatLng(37.496369 + 0.005, 126.957415 + 0.005)
        );
        googleMap.setLatLngBoundsForCameraTarget(bounds);

        //카메라 위치 설정
        LatLng soongsil = new LatLng(37.496369, 126.957415);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(soongsil, 16));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        setCustomMarkerView();
        getMarkerItems();
    }
    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker, null);
        tv_marker = marker_root_view.findViewById(R.id.map_marker);
    }

    private void getMarkerItems(){
        ArrayList<MarkerLocation> markerlist = new ArrayList<>();

        markerlist.add(new MarkerLocation(37.4966196, 126.957354, "백마상"));
        markerlist.add(new MarkerLocation(37.496540, 126.954292, "문화관"));
        markerlist.add(new MarkerLocation(37.4964874, 126.9551682, "숭덕경상관"));
        markerlist.add(new MarkerLocation(37.4957097, 126.9550571, "안익태기념관"));
        markerlist.add(new MarkerLocation(37.4957653, 126.9561681, "형남공학관"));
        markerlist.add(new MarkerLocation(37.496478, 126.956298, "베어드홀"));
        markerlist.add(new MarkerLocation(37.4955153, 126.9570569, "기독교박물관"));
        markerlist.add(new MarkerLocation(37.4955709, 126.9575846, "한경직기념관"));
        markerlist.add(new MarkerLocation(37.494898, 126.956505, "고민사거리"));
        markerlist.add(new MarkerLocation(37.4972373, 126.9562792, "대운동장"));
        markerlist.add(new MarkerLocation(37.496883, 126.956135, "학생회관"));
        markerlist.add(new MarkerLocation(37.4977928, 126.956168, "백마관"));
        markerlist.add(new MarkerLocation(37.4977651, 126.9568346, "교육관"));
        markerlist.add(new MarkerLocation(37.497549, 126.957460, "벤처중소기업"));
        markerlist.add(new MarkerLocation(37.497235, 126.958238, "조만식기념관"));
        markerlist.add(new MarkerLocation(37.496888, 126.9574965, "진리관"));
        markerlist.add(new MarkerLocation(37.4967694, 126.958415, "웨스터민스터홀"));
        markerlist.add(new MarkerLocation(37.4962097, 126.9585845, "중앙도서관"));
        markerlist.add(new MarkerLocation(37.4960259, 126.9581436, "신양관"));
        markerlist.add(new MarkerLocation(37.495619, 126.958465, "미래관"));
        markerlist.add(new MarkerLocation(37.495372, 126.958433, "농구장"));
        markerlist.add(new MarkerLocation(37.496249, 126.959206, "연구관"));
        markerlist.add(new MarkerLocation(37.495917, 126.959925, "창신관"));
        markerlist.add(new MarkerLocation(37.4954321, 126.9596122, "전산관"));
        markerlist.add(new MarkerLocation(37.4956817, 126.9604895, "글로벌브레인홀"));
        markerlist.add(new MarkerLocation(37.495075, 126.960334, "레지던스홀"));
        markerlist.add(new MarkerLocation(37.4946403, 126.9594151, "창의관"));
        markerlist.add(new MarkerLocation(37.4944064, 126.9599747, "정보과학관"));
        markerlist.add(new MarkerLocation(37.495861, 126.953991, "숭실대입구역"));
        markerlist.add(new MarkerLocation(37.496170, 126.955054, "돌계"));
        markerlist.add(new MarkerLocation(37.497056, 126.958345, "나계"));



        for(MarkerLocation markerLocation : markerlist) {
            getCount(markerLocation.tag, new OnCountCompleteListener() {
                @Override
                public void onCountComplete(int count) {
                    if(count>0) {
                        addMarker(markerLocation, false);
                    } else {
                        Log.d(TAG, "해당 value의 문서가 없음");
                    }
                }
            });
        }
    }

    private Marker addMarker(MarkerLocation markerLocation, boolean isMarkerSelected){
        LatLng position = new LatLng(markerLocation.getLat(), markerLocation.getLon());
        String tag = markerLocation.getTag();
        Log.d(TAG,tag);
        getCount(tag, new OnCountCompleteListener() {
            @Override
            public void onCountComplete(int count) {
                Log.d(TAG, String.valueOf(count));
                String DocumetCount = Integer.toString(count);
                tv_marker.setText(DocumetCount);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(tag);
                markerOptions.position(position);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapsActivity.this, marker_root_view)));

                if (isMarkerSelected) {
                    adapter.items.clear();
                    getAllDocumentsInACollection(tag);
                    mapInfoLayout.setVisibility(View.VISIBLE);
                    adapter.setOnItemClickListener(new MapAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FindPost findPost) {
                            // 항목 클릭을 처리하고, 새로운 액티비티 시작
                            Intent intent = new Intent(MapsActivity.this, FindClickActivity.class);
                            intent.putExtra("name", findPost.getName());
                            intent.putExtra("location", findPost.getLocation());
                            intent.putExtra("location_detail", findPost.getLocation_detail());
                            intent.putExtra("date", findPost.getDate());
                            intent.putExtra("more", findPost.getMore());
                            intent.putExtra("image", findPost.getImage());

                            startActivity(intent);
                        }
                    });

                }else {
                    mapInfoLayout.setVisibility(View.INVISIBLE);
                }
                mMap.addMarker(markerOptions);
            }

        });

        return null;
    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String tag = marker.getTitle();
        Log.d(TAG,tag);
        MarkerLocation temp = new MarkerLocation(lat, lon, tag);
        return addMarker(temp, isSelectedMarker);
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
        changeSelectedMarker(marker);
        marker.showInfoWindow();
        return true;
    }

    private void changeSelectedMarker(Marker marker){
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }
        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            //marker.remove();
        }
    }

    @Override
    public void onMapClick(LatLng latLng){
        mapInfoLayout.setVisibility(View.INVISIBLE);
        changeSelectedMarker(null);
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
                                String documentuid=document.getString("documentuid");
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                FindPost findPost=new FindPost(name,location,location_detail, date, more, image,documentuid);

                                findpost_map_info = findViewById(R.id.map_info_rv);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                findpost_map_info.setLayoutManager(layoutManager);

                                findpost_map_info.setAdapter(adapter);
                                adapter = new MapAdapter(MapsActivity.this,list);
                                adapter.addItem(findPost);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getCount(String tag, OnCountCompleteListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("FindPost")
                .whereEqualTo("location", tag)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            count = task.getResult().size();
                            listener.onCountComplete(count);
                        } else {
                            listener.onCountComplete(0);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public interface OnCountCompleteListener {
        void onCountComplete(int count);
    }
}