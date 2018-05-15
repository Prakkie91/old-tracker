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

public class LoginActivity extends BaseActivity {

    @BindView(R.id.emailET)
    EditText emailET;

    @BindView(R.id.passwordET)
    EditText passworkET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick (R.id.signupTV) public void onSignup() {
        startActivity(new Intent(mContext, SignupActivity.class));
    }

    @OnClick (R.id.loginBtn) public void onLogin() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }
}
