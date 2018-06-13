package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.R;
import com.odelan.track.data.model.User;
import com.odelan.track.ui.base.BaseActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.emailET)
    EditText emailET;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.sendBtn) public void onSend() {
        if (emailET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_input_email));
            return;
        }

        showLoading();
        AndroidNetworking.post(SERVER_URL + "user/forgot_password")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("email", emailET.getText().toString())
                .setTag("forgotPassword")
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
                                String message = response.getString("message");
                                //showToast(getString(R.string.sent_email));
                                showToast(message);
                                startActivity(new Intent(mContext, ResetPasswordActivity.class));
                                finish();
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
                        // handle error
                        dismissLoading();
                        showToast(getString(R.string.network_error));
                    }
                });
    }

    @OnClick(R.id.backIV) public void onBack() {
        finish();
    }
}
