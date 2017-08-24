package com.itshareplus.googlemapdemo;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Adapter.CustomSpinnerScanerAdapter;
import Models.MarkerObject;
import Modules.GPSTracker;
import Modules.ScanerRestaurantListener;
import Modules.ScanerRestaurant;

public class ScanerFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, ScanerRestaurantListener, AdapterView.OnItemSelectedListener {
    private static View view;
    MapView mMapView;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    ProgressDialog progressDialog;
    FloatingActionButton floatButton;
    private Marker currentPos;
    private List<Marker> listRestaurantMarker;
    final static String TAG = "SCANner";
    private Spinner spinner;
    private int[] icon_Place = {R.drawable.burger1, R.drawable.busstation, R.drawable.drug};
    private String[] arrPlace;
    private String currentChoosen="";
    private int currentItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_scaner, container, false);
        } catch (InflateException e) {
        }
        mMapView = (MapView) view.findViewById(R.id.scanerMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        floatButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanAround();
            }
        });

        spinner = (Spinner) view.findViewById(R.id.spinnerScaner);
        arrPlace = getResources().getStringArray(R.array.spinner);


        CustomSpinnerScanerAdapter customSpinner = new CustomSpinnerScanerAdapter(getActivity(), arrPlace, icon_Place,R.layout.custom_cell_spinner);
        currentChoosen=arrPlace[0];
        currentItem=0;
        spinner.setAdapter(customSpinner);
        spinner.setOnItemSelectedListener(this);


        return view;
    }

    private void scanAround() {
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        Location l = gpsTracker.getLocation();
        if (l != null) {

            if (currentPos != null) {
                currentPos.remove();
            }
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            LatLng current = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 18));
            currentPos = mMap.addMarker(new MarkerOptions()
                    .position(current));

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                new ScanerRestaurant(this, currentChoosen, 300, lat, lon).excute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Don't Support API.", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        Location l = gpsTracker.getLocation();
        LatLng location = new LatLng(l.getLatitude(), l.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Me")
                .position(location)));
        mMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }

    @Override
    public void onScanerStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait.",
                "Finding "+currentChoosen+" ..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        mMap.clear();


    }

    @Override
    public void onScanerSuccess(List<MarkerObject> points) {
        progressDialog.dismiss();
        String s = String.valueOf(points.size());
        for (MarkerObject markerRestaurant : points) {
            switch (currentItem){
                case 0:
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.burger1))
                            .title(markerRestaurant.getTitles().toString())
                            .position(markerRestaurant.getLatLng()));
                    break;
                case 1:
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.busstation))
                            .title(markerRestaurant.getTitles().toString())
                            .position(markerRestaurant.getLatLng()));
                    break;
                case 2:
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.drug))
                            .title(markerRestaurant.getTitles().toString())
                            .position(markerRestaurant.getLatLng()));
                    break;
            }

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentChoosen=arrPlace[position];
        currentItem=position;

        Toast.makeText(getActivity(),arrPlace[position]+position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
