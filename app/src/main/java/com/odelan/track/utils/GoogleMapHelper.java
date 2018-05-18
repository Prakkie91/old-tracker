package com.odelan.track.utils;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odelan.track.MyApplication;

import java.util.List;

public class GoogleMapHelper {

    Activity mContext = null;
    GoogleMap mMap = null;

    public GoogleMapHelper() {}

    public GoogleMapHelper(GoogleMap map) {
        mMap = map;
    }

    public GoogleMapHelper(Activity con, GoogleMap map) {
        mContext = con;
        mMap = map;
    }

    /**
     * Add Marker part
     *
     * @param lat
     * @param lng
     * @return
     */
    public Marker addMaker(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions markerOpt = new MarkerOptions().position(latLng);//.snippet("click then selected position");
        Marker marker = mMap.addMarker(markerOpt);
        return marker;
    }

    public Marker addMaker(double lat, double lng, String title) {
        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions markerOpt = new MarkerOptions().position(latLng);//.snippet("click then selected position");
        markerOpt.title(title);
        Marker marker = mMap.addMarker(markerOpt);
        return marker;
    }

    public Marker addMaker(LatLng latlng) {

        MarkerOptions markerOpt = new MarkerOptions().position(latlng);//.snippet("click then selected position");
        Marker marker = mMap.addMarker(markerOpt);
        return marker;
    }

    public Marker addMaker(LatLng latlng, String title) {

        MarkerOptions markerOpt = new MarkerOptions().position(latlng);//.snippet("click then selected position");
        markerOpt.title(title);
        Marker marker = mMap.addMarker(markerOpt);
        return marker;
    }

    /**
     * Add Circle part
     *
     * @param lat
     * @param lng
     * @param radius (m)
     * @return
     */
    // radius: m
    public Circle addCircle(double lat, double lng, double radius) {
        LatLng latlng = new LatLng(lat, lng);
        return mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(Color.BLUE)
                .fillColor(Color.RED));
    }

    public Circle addCircle(double lat, double lng, double radius, int strokeColor, int fillColor) {
        LatLng latlng = new LatLng(lat, lng);
        return mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(strokeColor)
                .fillColor(fillColor));
    }

    public Circle addCircle(LatLng latlng, double radius) {
        return mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(Color.BLUE)
                .strokeWidth(2.0f)
                .fillColor(Color.parseColor("#500000ff")));
    }

    public Circle addCircle(LatLng latlng, double radius, int strokeColor, int fillColor) {
        return mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(strokeColor)
                .strokeWidth(2.0f)
                .fillColor(fillColor));
    }

    public Circle addCircle(LatLng latlng, double radius, int strokeColor, float strokeWidth, int fillColor) {
        return mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(radius)
                .strokeColor(strokeColor)
                .strokeWidth(strokeWidth)
                .fillColor(fillColor));
    }

    /**
     * Move Camera on google map
     *
     * @param lat
     * @param lng
     * @param zoomLevel (float)
     */
    public void moveCameraPoint(double lat, double lng, float zoomLevel) {
        LatLng latlng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
    }

    public void moveCameraPoint(double lat, double lng) {
        LatLng latlng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));
    }

    public void moveCameraPoint(LatLng latlng, float zoomLevel) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
    }

    public void moveCameraPoint(LatLng latlng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));
    }

    public void animateCameraPoint(double lat, double lng, float zoomLevel) {
        LatLng latlng = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
    }

    public void animateCameraPoint(double lat, double lng) {
        LatLng latlng = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));
    }

    public void animateCameraPoint(LatLng latlng, float zoomLevel) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
    }

    public void animateCameraPoint(LatLng latlng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));
    }

    /**
     * Move Camera to bounds
     *
     * @param points
     * @param paddingPix (pixel)
     */
    public void moveCameraBounds(List<LatLng> points, int paddingPix) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : points) {
            builder.include(p);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), paddingPix));
    }

    public void moveCameraBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : points) {
            builder.include(p);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
    }

    public void moveCameraBounds(LatLngBounds bounds) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    public void moveCameraBounds(LatLngBounds bounds, int paddingPix) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, paddingPix));
    }

    public void animateCameraBounds(List<LatLng> points, int paddingPix) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : points) {
            builder.include(p);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), paddingPix));
    }

    public void animateCameraBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : points) {
            builder.include(p);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
    }
}
