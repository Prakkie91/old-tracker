package com.odelan.track.ui.activity.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.views.AccountView;
import com.odelan.track.ui.activity.Main.views.HomeView;
import com.odelan.track.ui.activity.Main.views.OrdersView;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.Common;
import com.odelan.track.utils.GPSTracker;

import java.util.ArrayList;

import butterknife.BindView;
import devlight.io.library.ntb.NavigationTabBar;
import im.delight.android.location.SimpleLocation;

import static com.odelan.track.MyApplication.g_latitude;
import static com.odelan.track.MyApplication.g_longitude;
import static com.odelan.track.MyApplication.g_speed;
import static com.odelan.track.MyApplication.g_status;
import static com.odelan.track.MyApplication.gpsInterval;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.vp_horizontal_ntb)
    ViewPager viewPager;
    @BindView(R.id.ntb_horizontal)
    NavigationTabBar navigationTabBar;

    HomeView homeView;
    OrdersView ordersView;
    AccountView accountView;

    //public SimpleLocation location;

    public int selectedPage = 0;

    private static final int GPS_PERMISSION_REQUEST_CODE = 9999;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    Bundle savedInstanceState;
    GPSTracker1 tracker;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.savedInstanceState = savedInstanceState;

        initUI(savedInstanceState);

        if (!hasPermissionsGranted(PERMISSIONS)) {
            requestPermissions(PERMISSIONS);
        } else {
            tracker = new GPSTracker1(this);
        }

        /*boolean requireFineGranularity = true;
        boolean passiveMode = false;
        boolean requireNewLocation = false;
        location = new SimpleLocation(this, requireFineGranularity, passiveMode, gpsInterval * 1000, requireNewLocation);

        if (!hasPermissionsGranted(PERMISSIONS)) {
            requestPermissions(PERMISSIONS);
        } else {
            location.beginUpdates();
            initUI(savedInstanceState);
        }

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        location.setListener(new SimpleLocation.Listener() {

            public void onPositionChanged() {
                // new location data has been received and can be accessed

                long time = 0;
                long preTime = 0;
                double lat = 0;
                double lng = 0;

                try {
                    time = System.currentTimeMillis();
                    preTime = Long.valueOf(getValueFromKey("time"));
                    lat = Double.valueOf(getValueFromKey("lat"));
                    lng = Double.valueOf(getValueFromKey("lng"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                g_latitude = location.getLatitude();
                g_longitude = location.getLongitude();

                double speed = 0;
                if (preTime != 0 && lat != 0 && lng != 0) {
                    double dist = Common.calculateDistance(lat, lng, g_latitude, g_longitude);
                    speed = (dist / (time - preTime) * 1000);
                    Log.d(TAG, "onPositionChanged: distance: " + dist + " speed: " + speed);
                }

                //g_speed = location.getSpeed(); // m/s
                g_speed = (float)speed;

                saveKeyValue("time", String.valueOf(System.currentTimeMillis()));
                saveKeyValue("lat", String.valueOf(location.getLatitude()));
                saveKeyValue("lng", String.valueOf(location.getLongitude()));
            }
        });*/
    }

    public void calSpeed(Location location) {
        long time = 0;
        long preTime = 0;
        double lat = 0;
        double lng = 0;

        try {
            time = System.currentTimeMillis();
            preTime = Long.valueOf(getValueFromKey("time"));
            lat = Double.valueOf(getValueFromKey("lat"));
            lng = Double.valueOf(getValueFromKey("lng"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        g_latitude = location.getLatitude();
        g_longitude = location.getLongitude();

        double speed = 0;
        if (preTime != 0 && lat != 0 && lng != 0) {
            double dist = Common.calculateDistance(lat, lng, g_latitude, g_longitude);
            speed = (dist / (time - preTime) * 1000);
            //speed = (dist / (time - preTime));
            Log.d(TAG, "onPositionChanged: distance: " + dist + " speed: " + speed);

            showToast("lat=" + g_latitude + " lng=" + g_longitude + " speed=" + speed + "m/s");
        }

        //g_speed = location.getSpeed(); // m/s
        g_speed = speed;

        saveKeyValue("time", String.valueOf(System.currentTimeMillis()));
        saveKeyValue("lat", String.valueOf(location.getLatitude()));
        saveKeyValue("lng", String.valueOf(location.getLongitude()));
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
        ActivityCompat.requestPermissions(mContext, permissions, GPS_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();

        // make the device update its location
        //location.beginUpdates();   // ...

        if (selectedPage == 1) {
            ordersView.getAllOrders();
        }
    }

    @Override
    public void onPause() {
        // stop location updates (saves battery)
        //location.endUpdates();

        super.onPause();
    }

    private void initUI(final Bundle savedInstanceState) {
        homeView = new HomeView(HomeActivity.this, savedInstanceState);
        ordersView = new OrdersView(HomeActivity.this);
        accountView = new AccountView(HomeActivity.this);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                if (position == 0) {
                    view = homeView.mContainerView;
                } else if (position == 1) {
                    view = ordersView.mContainerView;
                } else if (position == 2) {
                    view = accountView.mContainerView;
                }

                container.addView(view);
                return view;
            }
        });

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        getResources().getColor(R.color.background_btn_color_pressed))
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title(getString(R.string.home))
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        getResources().getColor(R.color.background_btn_color_pressed))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title(getString(R.string.orders))
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        getResources().getColor(R.color.background_btn_color_pressed))
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title(getString(R.string.account))
                        .badgeTitle("")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setIconSizeFraction(0.4f);
        navigationTabBar.setActiveColor(getResources().getColor(R.color.background_btn_color));
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                //navigationTabBar.getModels().get(position).hideBadge();
                selectedPage = position;
                if (position == 0) {
                    homeView.hasAcceptedOrder();
                }

                if (position == 1) {
                    ordersView.getAllOrders();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        /*navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(hasPermissionsGranted(PERMISSIONS)) {
                        //location.beginUpdates();
                        tracker = new GPSTracker1(this);
                    } else {
                        showPermissionError();
                    }
                } else {
                    showPermissionError();
                }
            }
        }

        if ((homeView != null) && (homeView.onRequestPermissionsResult(requestCode, permissions, grantResults))) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {

        }
    }

    public class GPSTracker1 extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        public boolean isGPSEnabled = false;

        // flag for network status
        public boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 1 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 100;//1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker1(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return location;
            }

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location = location;
            //locationManager.removeUpdates(this);
            calSpeed(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }
}
