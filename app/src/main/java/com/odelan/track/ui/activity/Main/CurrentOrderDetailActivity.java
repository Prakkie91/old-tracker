package com.odelan.track.ui.activity.Main;

import android.os.Bundle;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrentOrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail);

        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick (R.id.backIV) public void onBack() {
        finish();
    }

    @OnClick (R.id.pickBtn) public void onPick() {
        showToast("Picked");
    }

    @OnClick (R.id.deliverBtn) public void onDelivered() {
        showToast("Delivered");
    }

    @OnClick (R.id.cancelBtn) public void onCancel() {
        showToast("Cancelled");
    }
}
