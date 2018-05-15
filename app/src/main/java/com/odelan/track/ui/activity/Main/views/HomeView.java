package com.odelan.track.ui.activity.Main.views;

import android.content.Intent;
import android.view.LayoutInflater;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.CurrentOrderDetailActivity;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.LocationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeView extends BaseView {

    public HomeView(HomeActivity context) {
        super(context);

        mContainerView = LayoutInflater.from(
                mContext.getBaseContext()).inflate(R.layout.item_home, null, false);

        ButterKnife.bind(this, mContainerView);
    }

    @OnClick(R.id.orderBtn) public void onOrder() {
        mContext.startActivity(new Intent(mContext, CurrentOrderDetailActivity.class));
    }

    @OnClick(R.id.mapIV) public void onMap() {
        mContext.startActivity(new Intent(mContext, LocationActivity.class));
    }
}

