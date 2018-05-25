package com.odelan.track.ui.activity.Main;

import android.os.Bundle;
import android.widget.TextView;

import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    public static Order mOrder = null;

    @BindView(R.id.titleTV)
    TextView titleTV;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText(mOrder.title);
    }

    @OnClick (R.id.backIV) public void onBack() {
        finish();
    }

    @OnClick (R.id.acceptBtn) public void onAccept() {
        showToast(getString(R.string.accepted));
    }
}
