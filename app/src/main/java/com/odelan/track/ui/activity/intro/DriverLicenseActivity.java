package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverLicenseActivity extends BaseActivity {

    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.expireDateET)
    EditText expireDateET;

    @BindView(R.id.classET)
    EditText classET;


    @Override
    protected int getLayoutResID() {
        return R.layout.activity_driver_license;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.nextBtn) public void onNext() {
        startActivity(new Intent(mContext, VehicleActivity.class));
    }
}
