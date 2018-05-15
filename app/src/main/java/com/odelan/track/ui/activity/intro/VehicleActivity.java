package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleActivity extends BaseActivity {

    @BindView(R.id.typeET)
    EditText typeET;

    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.expireDateET)
    EditText expireDateET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick(R.id.nextBtn) public void onNext() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }
}
