package com.odelan.track;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.odelan.track.service.LocationService;
import com.odelan.track.utils.GPSTracker;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.io.File;
import java.util.List;


/**
 * Created by Administrator on 6/28/2016.
 */
public class MyApplication extends Application {

    public static final String SERVER_URL = "http://ec2-34-229-142-61.compute-1.amazonaws.com/DriverTrack/api/";
    public static final String X_API_KEY = "anonymous";

    public static String g_address = "";
    public static double g_latitude = 0;
    public static double g_longitude = 0;
    public static float g_speed = 0;
    public static String g_status = "invalid"; // 'valid' , 'invalid'
    public static int gpsInterval = 60; //second 1min
    public static boolean isGPSServiceRunning = false;

    public static Intent locationServiceintent;

    public static GPSTracker g_GPSTracker = null;

    public static File g_VehiclePhoto;
    public static File g_NumberPhoto;

    public static String one_id_android = "";

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());

        OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new QworkNotificationReceivedHandler())
                .setNotificationOpenedHandler(new QworkNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();

        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        boolean subscriptionSetting = status.getSubscriptionStatus().getUserSubscriptionSetting();
        String userID = status.getSubscriptionStatus().getUserId();
        String pushToken = status.getSubscriptionStatus().getPushToken();

        one_id_android = userID;
    }

    private class QworkNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            Log.d("OneSignal_notification", "NotificationReceived");
            try {
                Intent intent = new Intent("com.odelan.track");
                intent.putExtra("yourvalue", "torestore");
                sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GPSserviceStartExp", "Failed");
            }
        }
    }

    private class QworkNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            try {
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GPSserviceStartExp", "Failed");
            }
        }
    }

    public static void startGPSservice(Context con) {
        MyApplication.locationServiceintent = new Intent(con, LocationService.class);
        con.startService(MyApplication.locationServiceintent);
    }

    public static void stopGPSservice(Context con) {
        if (locationServiceintent != null) {
            con.stopService(MyApplication.locationServiceintent);
        }
    }

    public static boolean isAppForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }

    /** Round Drawables **/

    public static Drawable getRectDrawable(Activity activity, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getRoundDrawable(Activity activity, int cornerRadius, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(getScaledLength(activity, cornerRadius));
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getRoundDrawable(Activity activity, int cornerRadius, int solidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(getScaledLength(activity, cornerRadius));
        drawable.setColor(solidColor);
        return drawable;
    }

    public static Drawable getOvalDrawable(Activity activity, int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(solidColor);
        drawable.setStroke(getScaledLength(activity, strokeWidth), strokeColor);
        return drawable;
    }

    public static Drawable getOvalDrawable(Activity activity, int solidColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(solidColor);
        return drawable;
    }

    public static int getScaledLength(Activity activity, int length) {
        int scaledLength = (int)(length * activity.getResources().getDisplayMetrics().density);
        return scaledLength;
    }
}


/** Android Networking useful */

/* Get
AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
        .addPathParameter("pageNumber", "0")
        .addQueryParameter("limit", "3")
        .addHeaders("token", "1234")
        .setTag("test")
        .setPriority(Priority.LOW)
        .build()
        .getAsJSONArray(new JSONArrayRequestListener() {
@Override
public void onResponse(JSONArray response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/
/* Post
AndroidNetworking.post("https://fierce-cove-29863.herokuapp.com/createAnUser")
        .addBodyParameter("firstname", "Amit")
        .addBodyParameter("lastname", "Shekhar")
        .setTag("test")
        .setPriority(Priority.MEDIUM)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
@Override
public void onResponse(JSONObject response) {
        // do anything with response
        }
@Override
public void onError(ANError error) {
        // handle error
        }
        });
*/