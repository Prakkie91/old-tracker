package com.odelan.track.ui.activity.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.GPSTracker;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    final int REQUEST_PERMISSION = 999;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ButterKnife.bind(this);
        mContext = this;

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);

        readyGPS();

        /** Check Permission **/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION);
            }
        }
    }

    public void readyGPS() {
        if(mContext != null) {
            MyApplication.g_GPSTracker = new GPSTracker(mContext);
            if (MyApplication.g_GPSTracker.canGetLocation()) {
                MyApplication.g_latitude = MyApplication.g_GPSTracker.getLatitude(); // returns latitude
                MyApplication.g_longitude = MyApplication.g_GPSTracker.getLongitude(); // returns longitude
                //showToast("lat: " + MyApplication.g_latitude + ", lang: " + MyApplication.g_longitude);
            } else {
                MyApplication.g_GPSTracker.showSettingsAlert();
            }
        }
    }

    private boolean checkPermission () {
        boolean hasPermision = true;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            hasPermision = false;
        }
        return hasPermision;
    }

    private void showPermissionError() {
        new MaterialDialog.Builder(this)
                .title("Warning")
                .content("Permissions were denied. In order to allow permission, first you have to turn off screen overlay from Settings > Apps.")
                .positiveText("OK")
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(checkPermission()) {
                        readyGPS();
                    } else {
                        showPermissionError();
                    }

                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                } else {
                    //showToast("Recording permission was denied");
                    showPermissionError();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermissions();
            return;
        }

        googleMap.setMyLocationEnabled(false);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        } else {

        }

        //locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    private void requestLocationPermissions () {
        ActivityCompat.requestPermissions((Activity)mContext,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                100);
    }

    @OnClick(R.id.backIV) public void onBack() {
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        MyApplication.g_latitude = location.getLatitude();
        MyApplication.g_longitude = location.getLongitude();
        LatLng latLng = new LatLng(MyApplication.g_latitude, MyApplication.g_longitude);
        System.out.println("latitude :::: " + MyApplication.g_latitude + " longitude :::: " + MyApplication.g_longitude);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        setLocation(MyApplication.g_latitude, MyApplication.g_longitude, "Driver Location");

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(MyApplication.g_latitude, MyApplication.g_longitude))
                .radius(100000)
                .strokeColor(Color.RED)
                .fillColor(Color.RED));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setLocation(double latitude, double longitude, String address){

        LatLng latLng = new LatLng(MyApplication.g_latitude, MyApplication.g_longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        MarkerOptions markerOpt = new MarkerOptions().position(latLng);//.snippet("click then selected position");

        markerOpt.title(address);
        Marker marker = googleMap.addMarker(markerOpt);
    }
}
