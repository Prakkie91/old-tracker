package com.odelan.track;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;
import com.androidnetworking.AndroidNetworking;
import com.odelan.track.utils.GPSTracker;

import java.util.List;


/**
 * Created by Administrator on 6/28/2016.
 */
public class MyApplication extends Application {

    public static String g_address = "";
    public static double g_latitude = 0;
    public static double g_longitude = 0;

    public static GPSTracker g_GPSTracker = null;

    LocalizationApplicationDelegate localizationDelegate = new LocalizationApplicationDelegate(this);

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localizationDelegate.onConfigurationChanged(this);
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
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