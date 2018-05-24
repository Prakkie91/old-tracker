package com.odelan.track.ui.activity.Main.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.LocationActivity;
import com.odelan.track.ui.activity.Main.OrderDetailActivity;
import com.odelan.track.utils.GPSTracker;
import com.odelan.track.utils.GoogleMapHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public class HomeView extends BaseView {

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.recordIV)
    ImageView recordIV;

    private GoogleMap googleMap;
    private GoogleMapHelper googleMapHelper;

    private static final int MAP_PERMISSION_REQUEST_CODE = 999;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    public List<Order> mData = new ArrayList<>();

    boolean isRecording = false;

    @Override
    protected int getLayoutResID() {
        return R.layout.item_home;
    }

    public HomeView(HomeActivity context, Bundle savedInstanceState) {
        super(context);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMapHelper = new GoogleMapHelper(mMap);
                googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                    @Override
                    public void onCircleClick(Circle circle) {
                        mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                        if (circle.getTag().toString() != null) {
                            OrderDetailActivity.mOrder = getOrderWithID(circle.getTag().toString());
                        }
                    }
                });

                if (!hasPermissionsGranted(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS);
                } else {
                    readyGPS();
                }
            }
        });

        recordIV.setImageResource(R.drawable.ic_record_stoped);
    }

    @SuppressLint("MissingPermission")
    public void readyGPS() {
        if(mContext != null) {
            MyApplication.g_GPSTracker = new GPSTracker(mContext);
            if (MyApplication.g_GPSTracker.canGetLocation()) {
                MyApplication.g_latitude = MyApplication.g_GPSTracker.getLatitude();
                MyApplication.g_longitude = MyApplication.g_GPSTracker.getLongitude();

                googleMap.setMyLocationEnabled(true);
                showData();
            } else {
                MyApplication.g_GPSTracker.showSettingsAlert();
            }
        }
    }

    public void getData() {
        mData = new ArrayList<>();

        Order item = new Order();
        item.oid = "1";
        item.title = "Order1";
        item.lat = MyApplication.g_latitude - 0.1;
        item.lng = MyApplication.g_longitude - 0.1;
        item.radius = 5000;
        mData.add(item);

        item = new Order();
        item.oid = "2";
        item.title = "Order2";
        item.lat = MyApplication.g_latitude -0.1515;
        item.lng = MyApplication.g_longitude + 0.1212;
        item.radius = 10000;
        mData.add(item);

        item = new Order();
        item.oid = "3";
        item.title = "Order3";
        item.lat = MyApplication.g_latitude + 0.3;
        item.lng = MyApplication.g_longitude - 0.3;
        item.radius = 8000;
        mData.add(item);
    }

    public Order getOrderWithID(String oid) {
        for (Order o : mData) {
            if (o.oid == oid) {
                return o;
            }
        }
        return null;
    }

    private void showData() {

        getData();
        LatLng myLocation = new LatLng(MyApplication.g_latitude, MyApplication.g_longitude);
        googleMapHelper.addMaker(myLocation);

        List<LatLng>bounds = new ArrayList<>();
        bounds.add(myLocation);

        for (Order item : mData) {
            LatLng latLng = new LatLng(item.lat, item.lng);
            bounds.add(latLng);

            Circle circle = googleMapHelper.addCircle(latLng, item.radius);
            circle.setTag(item.oid);
            circle.setClickable(true);
        }

        googleMapHelper.moveCameraPoint(myLocation, 9);
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

    @OnClick(R.id.recordIV) public void onMap() {
        if (isRecording) {
            isRecording = false;
            recordIV.setImageResource(R.drawable.ic_record_stoped);
            mContext.showToast("Recording Stopped.");
        } else {
            isRecording = true;
            recordIV.setImageResource(R.drawable.ic_record_started);
            mContext.showToast("Recording Started.");
        }
    }

    @OnClick(R.id.startBtn) public void onStart() {
        mContext.showToast("Starting...");
    }

    @OnClick(R.id.stopBtn) public void onStop() {
        mContext.showToast("Stopping...");
    }
}

