package com.odelan.track.ui.activity.Main.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.OrderDetailActivity;
import com.odelan.track.utils.GPSTracker;
import com.odelan.track.utils.GoogleMapHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public class HomeView extends BaseView {

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.startBtn)
    Button startBtn;

    @BindView(R.id.stopBtn)
    Button stopBtn;

    private GoogleMap googleMap;
    private GoogleMapHelper googleMapHelper;

    private static final int MAP_PERMISSION_REQUEST_CODE = 999;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    public List<Order> mData = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.item_home;
    }

    public HomeView(HomeActivity context,  Bundle savedInstanceState) {
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
        item.title = mContext.getString(R.string.order)+1;
        //item.lat = MyApplication.g_latitude - 0.1;
        //item.lng = MyApplication.g_longitude - 0.1;
        item.lat = 22.2982372;
        item.lng = 114.1695309;
        item.radius = 1000;
        mData.add(item);

        item = new Order();
        item.oid = "2";
        item.title = mContext.getString(R.string.order)+2;
        //item.lat = MyApplication.g_latitude -0.1515;
        //item.lng = MyApplication.g_longitude + 0.1212;
        item.lat = 22.3193429;
        item.lng = 114.1589395;
        item.radius = 500;
        mData.add(item);

        item = new Order();
        item.oid = "3";
        item.title = mContext.getString(R.string.order)+3;
        //item.lat = MyApplication.g_latitude + 0.3;
        //item.lng = MyApplication.g_longitude - 0.3;
        item.lat = 22.2825059;
        item.lng = 114.1830503;
        item.radius = 800;
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
        //LatLng myLocation = new LatLng(MyApplication.g_latitude, MyApplication.g_longitude);
        //googleMapHelper.addMaker(myLocation);

        List<LatLng>bounds = new ArrayList<>();
        //bounds.add(myLocation);

        for (Order item : mData) {
            LatLng latLng = new LatLng(item.lat, item.lng);
            bounds.add(latLng);

            Circle circle = googleMapHelper.addCircle(latLng, item.radius);
            circle.setTag(item.oid);
            circle.setClickable(true);
            //googleMapHelper.addMaker(latLng);
            addCustomMarker(latLng);
        }

        //googleMapHelper.moveCameraPoint(myLocation, 9);
        googleMapHelper.moveCameraBounds(bounds, 100);
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
                .title(mContext.getString(R.string.warning))
                .content(mContext.getString(R.string.permission_denied))
                .positiveText(mContext.getString(R.string.ok))
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

    @OnClick(R.id.startBtn) public void onStart() {
        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
        mContext.showToast(mContext.getString(R.string.recording));
    }

    @OnClick(R.id.stopBtn) public void onStop() {
        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
        mContext.showToast(mContext.getString(R.string.stopped));
    }

    private void addCustomMarker(LatLng latlng) {
        if (googleMap == null) {
            return;
        }

        View mCustomMarkerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);

        // adding a marker on map with image from  drawable
        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, mContext.getString(R.string.ad_here)))));

        // adding a marker with image from URL using glide image loading library
        /*Glide.with(getApplicationContext()).
                load(ImageUrl)
                .asBitmap()
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        mMarkerTextView.setText("Ad here");
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(mDummyLatLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, bitmap))));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDummyLatLng, 13f));


                    }
                });*/


    }

    private Bitmap getMarkerBitmapFromView(View view, String title) {
        ImageView mMarkerImageView = (ImageView) view.findViewById(R.id.iv);
        TextView mMarkerTextView = (TextView) view.findViewById(R.id.tv);
        mMarkerTextView.setText(title);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    /*private Bitmap getMarkerBitmapFromView(View view, @DrawableRes int resId) {

        mMarkerImageView.setImageResource(resId);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }*/
}

