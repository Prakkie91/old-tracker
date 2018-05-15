package com.odelan.track.ui.activity.Main;

import android.os.Bundle;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick (R.id.backIV) public void onBack() {
        finish();
    }

    @OnClick (R.id.acceptBtn) public void onAccepted() {
        showToast("Accepted");
    }

    @OnClick (R.id.cancelBtn) public void onCancel() {
        showToast("Cancelled");
    }
}
