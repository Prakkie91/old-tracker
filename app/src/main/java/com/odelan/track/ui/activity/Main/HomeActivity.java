package com.odelan.track.ui.activity.Main;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.views.AccountView;
import com.odelan.track.ui.activity.Main.views.HomeView;
import com.odelan.track.ui.activity.Main.views.OrdersView;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.Common;

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

    public SimpleLocation location;

    public int selectedPage = 0;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI(savedInstanceState);

        boolean requireFineGranularity = false;
        boolean passiveMode = false;
        boolean requireNewLocation = false;
        location = new SimpleLocation(this, requireFineGranularity, passiveMode, gpsInterval * 1000, requireNewLocation);

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        location.beginUpdates();

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

                double speed = 0;
                if (preTime != 0 && lat != 0 && lng != 0) {
                    double dist = Common.calculateDistance(lat, lng, g_latitude, g_longitude);
                    speed = (dist / (time - preTime) * 1000);
                    Log.d(TAG, "onPositionChanged: distance: " + dist + " speed: " + speed);
                }

                g_latitude = location.getLatitude();
                g_longitude = location.getLongitude();
                //g_speed = location.getSpeed(); // m/s
                g_speed = (float)speed;

                saveKeyValue("time", String.valueOf(System.currentTimeMillis()));
                saveKeyValue("lat", String.valueOf(location.getLatitude()));
                saveKeyValue("lng", String.valueOf(location.getLongitude()));
            }
        });
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
        if (homeView.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
