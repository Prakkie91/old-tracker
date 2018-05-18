package com.odelan.track.ui.activity.Main;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.views.HomeView;
import com.odelan.track.ui.activity.Main.views.OrdersView;
import com.odelan.track.ui.activity.Main.views.SettingsView;
import com.odelan.track.ui.activity.Main.views.TestView;
import com.odelan.track.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.vp_horizontal_ntb) ViewPager viewPager;
    @BindView(R.id.ntb_horizontal) NavigationTabBar navigationTabBar;

    HomeView homeView;
    OrdersView ordersView;
    SettingsView settingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        mContext = this;

        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        homeView = new HomeView(HomeActivity.this, savedInstanceState);
        ordersView = new OrdersView(HomeActivity.this);
        settingsView = new SettingsView(HomeActivity.this);

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
                    view = settingsView.mContainerView;
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
                        .title("Home")
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        getResources().getColor(R.color.background_btn_color_pressed))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Orders")
                        .badgeTitle("")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        getResources().getColor(R.color.background_btn_color_pressed))
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Settings")
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
