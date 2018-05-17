package com.odelan.track.ui.activity.Main.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.utils.GPSTracker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestView extends BaseView {

    @BindView(R.id.mapView)
    MapView mapView;

    private GoogleMap googleMap;

    private static final int MAP_PERMISSION_REQUEST_CODE = 999;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    public TestView(HomeActivity context, Bundle savedInstanceState) {
        super(context);

        mContainerView = LayoutInflater.from(
                mContext.getBaseContext()).inflate(R.layout.item_test, null, false);

        ButterKnife.bind(this, mContainerView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (!hasPermissionsGranted(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS);
                } else {
                    readyGPS();
                    //initMap();
                }
            }
        });
    }

    public void readyGPS() {
        if(mContext != null) {
            MyApplication.g_GPSTracker = new GPSTracker(mContext);
            if (MyApplication.g_GPSTracker.canGetLocation()) {
                MyApplication.g_latitude = MyApplication.g_GPSTracker.getLatitude(); // returns latitude
                MyApplication.g_longitude = MyApplication.g_GPSTracker.getLongitude(); // returns longitude
                //showToast("lat: " + MyApplication.g_latitude + ", lang: " + MyApplication.g_longitude);

                setLocation(MyApplication.g_latitude, MyApplication.g_longitude, "Driver Location");

                Circle circle = googleMap.addCircle(new CircleOptions()
                        .center(new LatLng(MyApplication.g_latitude, MyApplication.g_longitude))
                        .radius(100000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.RED));
            } else {
                MyApplication.g_GPSTracker.showSettingsAlert();
            }
        }
    }

    public void setLocation(double latitude, double longitude, String address){

        LatLng latLng = new LatLng(MyApplication.g_latitude, MyApplication.g_longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        MarkerOptions markerOpt = new MarkerOptions().position(latLng);//.snippet("click then selected position");

        markerOpt.title(address);
        Marker marker = googleMap.addMarker(markerOpt);
    }

    @SuppressLint("MissingPermission")
    public void initMap() {
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private boolean hasPermissionsGranted (String[] permissions) {
        boolean hasPermision = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermision = false;
            }
        }

        return hasPermision;
    }

    private void showPermissionError() {
        new MaterialDialog.Builder(mContext)
                .title("Warning")
                .content("Permissions were denied. In order to allow permission, first you have to turn off screen overlay from Settings > Apps.")
                .positiveText("OK")
                .show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(mContext, permissions, MAP_PERMISSION_REQUEST_CODE);
    }

    public boolean onRequestPermissionsResult (int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MAP_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(hasPermissionsGranted(PERMISSIONS)) {
                        readyGPS();
                        //initMap();
                        return true;
                    } else {
                        showPermissionError();
                        return false;
                    }
                } else {
                    showPermissionError();
                    return false;
                }
            }
        }

        return false;
    }
}

