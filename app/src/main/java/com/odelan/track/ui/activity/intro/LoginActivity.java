package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.User;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.MultiLanguageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

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

        if (multiLanguageHelper.getCurrentLanguage() == null ||
                multiLanguageHelper.getCurrentLanguage().equals("") ||
                multiLanguageHelper.getCurrentLanguage().equals(MultiLanguageHelper.LANG_VAL_CHINA)) {
            zhTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
            enTV.setTextColor(getResources().getColor(R.color.white));
        } else {
            zhTV.setTextColor(getResources().getColor(R.color.white));
            enTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        }

        String email = getValueFromKey("email");
        if (email != null && !email.equals("")) {
            emailET.setText(email);
            passworkET.setText(getValueFromKey("password"));
        }

    }

    @OnClick(R.id.forgotPasswordTV) public void onForgotPassword() {
        startActivity(new Intent(mContext, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.signupTV)
    public void onSignup() {
        startActivity(new Intent(mContext, SignupActivity.class));
    }

    @OnClick(R.id.loginBtn)
    public void onLogin() {
        if (emailET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_input_email));
            return;
        }

        if (passworkET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_input_password));
            return;
        }

        showLoading();
        AndroidNetworking.post(SERVER_URL + "user/login")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("email", emailET.getText().toString())
                .addBodyParameter("password", passworkET.getText().toString())
                .setTag("login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONObject user = response.getJSONObject("data");
                                User me = LoganSquare.parse(user.toString(), User.class);
                                saveKeyValue("user", user.toString());

                                saveKeyValue("email", emailET.getText().toString());
                                saveKeyValue("password", passworkET.getText().toString());
                                startActivity(new Intent(mContext, HomeActivity.class));

                                //saveOneSignalId();
                            } else {
                                showToast(getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //showToast(getString(R.string.failed));

                            try {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    JSONObject user = response.getJSONObject("data");
                                    User me = LoganSquare.parse(user.toString(), User.class);
                                    saveKeyValue("user", user.toString());

                                    //saveOneSignalId();
                                } else {
                                    showToast(getString(R.string.failed));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                showToast(getString(R.string.failed));
                            }
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dismissLoading();
                        showToast(getString(R.string.network_error));
                    }
                });
    }

    public void saveOneSignalId() {
        AndroidNetworking.post("http://ec2-34-228-199-199.compute-1.amazonaws.com/RestDemo/api/noti/checkOneId")
                //.addQueryParameter("cookie", cookie)
                .addBodyParameter("oneid", MyApplication.one_id_android)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if(status == 1) {
                                //showToast("success");
                                saveKeyValue("email", emailET.getText().toString());
                                saveKeyValue("password", passworkET.getText().toString());
                                startActivity(new Intent(mContext, HomeActivity.class));
                            } else {
                                showToast(getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast(getString(R.string.failed));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        showToast(getString(R.string.network_error));
                    }
                });
    }

    @OnClick(R.id.zhTV) public void onZhTV() {
        zhTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        enTV.setTextColor(getResources().getColor(R.color.white));
        setLanguage(MultiLanguageHelper.LANG_VAL_CHINA);
    }

    @OnClick(R.id.enTV) public void onEnTV() {
        zhTV.setTextColor(getResources().getColor(R.color.white));
        enTV.setTextColor(getResources().getColor(R.color.txt_gray_color));
        setLanguage(MultiLanguageHelper.LANG_VAL_ENGLISH);
    }
}
