package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.EditText;
import android.widget.TextView;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.emailET)
    EditText emailET;

    @BindView(R.id.passwordET)
    EditText passworkET;

    @BindView(R.id.zhTV)
    TextView zhTV;

    @BindView(R.id.enTV)
    TextView enTV;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (multiLanguageHelper.getCurrentLanguage() == null || multiLanguageHelper.getCurrentLanguage().equals(multiLanguageHelper.LANG_VAL_CHINA)) {
            zhTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
            enTV.setTextColor(getResources().getColor(R.color.white));
        } else {
            zhTV.setTextColor(getResources().getColor(R.color.white));
            enTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        }
    }

    @OnClick(R.id.signupTV)
    public void onSignup() {
        startActivity(new Intent(mContext, SignupActivity.class));
    }

    @OnClick(R.id.loginBtn)
    public void onLogin() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    @OnClick(R.id.zhTV) public void onZhTV() {
        zhTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        enTV.setTextColor(getResources().getColor(R.color.white));
        setLanguage(multiLanguageHelper.LANG_VAL_CHINA);
    }

    @OnClick(R.id.enTV) public void onEnTV() {
        zhTV.setTextColor(getResources().getColor(R.color.white));
        enTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        setLanguage(multiLanguageHelper.LANG_VAL_ENGLISH);
    }
}
