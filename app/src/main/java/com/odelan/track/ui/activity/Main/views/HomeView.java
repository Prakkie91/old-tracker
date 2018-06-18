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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.AdRegionModel;
import com.odelan.track.data.model.User;
import com.odelan.track.service.LocationService;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.OrderDetailActivity;
import com.odelan.track.utils.GPSTracker;
import com.odelan.track.utils.GoogleMapHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;
import static com.odelan.track.MyApplication.g_status;
import static com.odelan.track.MyApplication.isGPSServiceRunning;



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

    public List<AdRegionModel> mData = new ArrayList<>();

    boolean isMapReady = false;
    String hasOrder = "false";

    double mLat = 0;
    double mLng = 0;

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
                            OrderDetailActivity.mOrderId = getAdRegionWithID(circle.getTag().toString()).order_id;
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                        if (marker.getTag().toString() != null) {
                            OrderDetailActivity.mOrderId = getAdRegionWithID(marker.getTag().toString()).order_id;
                        }
                        return false;
                    }
                });

                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        readyGPS();
                    }
                });

                /*if (!hasPermissionsGranted(PERMISSIONS)) {
                    requestPermissions(PERMISSIONS);
                } else {
                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                            readyGPS();
                        }
                    });
                }*/
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void readyGPS() {
        if(mContext != null) {
            MyApplication.g_GPSTracker = new GPSTracker(mContext);
            if (MyApplication.g_GPSTracker.canGetLocation()) {
                mLat = MyApplication.g_GPSTracker.getLatitude();
                mLng = MyApplication.g_GPSTracker.getLongitude();

                googleMap.setMyLocationEnabled(true);
                hasAcceptedOrder();
                //getAllRegions();
                isMapReady = true;
            } else {
                MyApplication.g_GPSTracker.showSettingsAlert();
            }
        }
    }

    public AdRegionModel getAdRegionWithID(String aid) {
        for (AdRegionModel am: mData) {
            if (am.aid.equals(aid)) {
                return am;
            }
        }
        return null;
    }

    public void hasAcceptedOrder() {
        User me = mContext.getMe();
        mContext.showLoading();
        AndroidNetworking.post(SERVER_URL + "order/hasAcceptedOrder")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("user_id", me.userid)
                .setTag("hasAcceptedOrder")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        mContext.dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                hasOrder = response.getString("data");
                                if (hasOrder.equals("true")) {
                                    if (isGPSServiceRunning) {
                                        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
                                        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
                                    } else {
                                        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
                                        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
                                    }
                                } else {
                                    g_status = "invalid";
                                    MyApplication.stopGPSservice(mContext);
                                    startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                                    stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                                }
                            } else {
                                mContext.showToast(mContext.getString(R.string.failed));
                                startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                                stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                                hasOrder = "false";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContext.showToast(mContext.getString(R.string.failed));
                            startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                            stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.gray_btn_background));
                            hasOrder = "false";
                        }

                        getAllRegions();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        mContext.dismissLoading();
                        mContext.showToast(mContext.getString(R.string.network_error));
                        startBtn.setBackgroundColor(Color.GRAY);
                        stopBtn.setBackgroundColor(Color.GRAY);
                        hasOrder = "false";
                    }
                });
    }

    public void getAllRegions() {
        if (!isMapReady) {
            return;
        }
        User me = mContext.getMe();
        mContext.showLoading();
        AndroidNetworking.post(SERVER_URL + "order/getAllSameTypeRegionsWithUserId")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("user_id", me.userid)
                .setTag("getAllSameTypeRegionsWithUserId")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        mContext.dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                mData = LoganSquare.parseList(response.getString("data"), AdRegionModel.class);
                                showData();
                            } else {
                                mContext.showToast(mContext.getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContext.showToast(mContext.getString(R.string.failed));
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        mContext.dismissLoading();
                        mContext.showToast(mContext.getString(R.string.network_error));
                    }
                });
    }

    private void showData() {

        List<LatLng>bounds = new ArrayList<>();
        //bounds.add(myLocation);

        for (AdRegionModel item : mData) {
            LatLng latLng = new LatLng(item.lat, item.lng);
            bounds.add(latLng);

            Circle circle = googleMapHelper.addCircle(latLng, item.radius);
            circle.setTag(item.aid);
            circle.setClickable(true);
            //googleMapHelper.addMaker(latLng);
            addCustomMarker(item.aid, latLng);
        }

        googleMapHelper.moveCameraPoint(new LatLng(mLat, mLng), 9);
        //googleMapHelper.moveCameraBounds(bounds, 100);
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
        if (hasOrder.equals("false")) {
            return;
        }

        g_status = "valid";

        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
        mContext.showToast(mContext.getString(R.string.recording));

        MyApplication.startGPSservice(mContext);
    }

    @OnClick(R.id.stopBtn) public void onStop() {
        if (hasOrder.equals("false")) {
            return;
        }

        g_status = "invalid";

        startBtn.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_background));
        stopBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_btn_background));
        mContext.showToast(mContext.getString(R.string.stopped));

        MyApplication.stopGPSservice(mContext);
    }

    private void addCustomMarker(String oid, LatLng latlng) {
        if (googleMap == null) {
            return;
        }

        View mCustomMarkerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);

        // adding a marker on map with image from  drawable
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, mContext.getString(R.string.ad_here)))));
        marker.setTag(oid);

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

