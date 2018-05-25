package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import java.sql.Driver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.nameET)
    EditText nameET;

    @BindView(R.id.idET)
    EditText idET;

    @BindView(R.id.phoneET)
    EditText phoneET;

    @BindView(R.id.passwordET)
    EditText passworkET;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.signupBtn) public void onSignup() {
        startActivity(new Intent(mContext, DriverLicenseActivity.class));
    }

    @OnClick (R.id.loginTV) public void onLogin() {
        finish();
    }
}
