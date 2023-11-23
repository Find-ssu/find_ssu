package com.example.find_ssu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.find_ssu.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_maps, container, false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);
    }

    //xml파일을 bitmap으로 변환하는 함수
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
        View marker_root_view = LayoutInflater.from(getActivity()).inflate(R.layout.marker, null);
        TextView tv_marker;
        tv_marker = marker_root_view.findViewById(R.id.map_marker);

        //법학관
        LatLng law_officer = new LatLng(37.496540, 126.954292);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions
                .position(law_officer)
                .anchor(0.5f, 0.5f) // polyline이 마커의 하단이 아닌 중앙을 꼭짓점으로 하도록 수정
                .title("법학관");
        View marker = LayoutInflater.from(getActivity()).inflate(R.layout.marker, null);
        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker_root_view))));

        //백마상
        LatLng white_horse_statue = new LatLng(37.4966196, 126.957354);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1
                .position(white_horse_statue)
                .anchor(0.5f, 0.5f)
                .title("백마상");
        View marker1 = LayoutInflater.from(getActivity()).inflate(R.layout.marker, null);
        mMap.addMarker(markerOptions1.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker_root_view))));
    }
}

