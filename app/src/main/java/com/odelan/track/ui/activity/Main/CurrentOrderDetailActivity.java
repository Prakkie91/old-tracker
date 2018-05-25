package com.odelan.track.ui.activity.Main;

import android.os.Bundle;
import android.widget.TextView;

import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrentOrderDetailActivity extends BaseActivity {

    public static Order mOrder = null;

    @BindView(R.id.titleTV)
    TextView titleTV;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_current_order_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleTV.setText(mOrder.title);
    }

    @OnClick (R.id.backIV) public void onBack() {
        finish();
    }

    @OnClick (R.id.cancelBtn) public void onCancel() {
        showToast(getString(R.string.cancelled));
    }
}
