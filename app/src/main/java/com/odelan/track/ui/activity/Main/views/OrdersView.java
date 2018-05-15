package com.odelan.track.ui.activity.Main.views;

import android.content.Intent;
import android.view.LayoutInflater;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.OrderDetailActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersView extends BaseView {

    public OrdersView(HomeActivity context) {
        super(context);

        mContainerView = LayoutInflater.from(
                mContext.getBaseContext()).inflate(R.layout.item_orders, null, false);

        ButterKnife.bind(this, mContainerView);
    }

    @OnClick(R.id.orderBtn) public void onOrder() {
        mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
    }
}
