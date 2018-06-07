package com.odelan.track.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.MyApplication;
import com.odelan.track.data.model.User;
import com.odelan.track.utils.Common;

import org.json.JSONObject;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;
import static com.odelan.track.MyApplication.g_latitude;
import static com.odelan.track.MyApplication.g_longitude;
import static com.odelan.track.MyApplication.g_speed;
import static com.odelan.track.MyApplication.g_status;
import static com.odelan.track.MyApplication.isGPSServiceRunning;

/**
 * Created by Administrator on 9/27/2017.
 */

public class LocationService extends IntentService {

    public LocationService() {
        super("LocationService");
    }

    @Override
    public void onDestroy() {
        isGPSServiceRunning = false;
        super.onDestroy();
    }

    Handler mHandler = new Handler();
    Runnable gpsRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("DriverTrack_GPSService", "GPS_Service running");
            isGPSServiceRunning = true;
            if(g_status.equals("valid")) {
                postLocation();
            } else {
                isGPSServiceRunning = false;
            }
            //Common.showToast(LocationService.this, "lat=" + MyApplication.g_latitude + "  lng=" + MyApplication.g_longitude + "  speed=" + MyApplication.g_speed + "Km");
            mHandler.postDelayed(gpsRunnable, MyApplication.gpsInterval*1000);
        }
    };

    public void postLocation() {
        User me = null;
        try {
            String userStr = Common.getInfoWithValueKey(this, "user");
            if (!userStr.equals("")) {
                me = LoganSquare.parse(userStr, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (me == null) {
            return;
        }

        AndroidNetworking.post(SERVER_URL + "order/saveRecord")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("user_id", me.userid)
                .addBodyParameter("lat", String.valueOf(g_latitude))
                .addBodyParameter("lng", String.valueOf(g_longitude))
                .addBodyParameter("speed", String.valueOf(g_speed))
                //.addBodyParameter("speed", "20")
                .addBodyParameter("status", g_status)
                .setTag("saveRecord")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                //Common.showToast(LocationService.this, "uploaded");
                                Common.showToast(LocationService.this,
                                        "lat=" + MyApplication.g_latitude
                                                + "  lng=" + MyApplication.g_longitude
                                                + "  speed=" + MyApplication.g_speed + "Km");
                            } else {
                                //Common.showToast(LocationService.this, "failed status = 0");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Common.showToast(LocationService.this, "failed exception occured");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //Common.showToast(LocationService.this, "failed network error");
                    }
                });
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mHandler.postDelayed(gpsRunnable, 5000);
    }
}
