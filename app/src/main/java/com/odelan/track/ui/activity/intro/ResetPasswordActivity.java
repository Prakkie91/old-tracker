package com.odelan.track.ui.activity.intro;

import android.os.Bundle;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.codeET)
    EditText codeET;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.repeatPasswordET)
    EditText repeatPasswordET;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.sendBtn) public void onSend() {
        if (codeET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_code));
            return;
        }

        if (passwordET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_input_password));
            return;
        }

        if (repeatPasswordET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_repeat_password));
            return;
        }

        if (!passwordET.getText().toString().equals(repeatPasswordET.getText().toString())) {
            showToast(getString(R.string.not_match_password));
            return;
        }

        showLoading();
        AndroidNetworking.post(SERVER_URL + "user/reset_password")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("code", codeET.getText().toString())
                .addBodyParameter("password", passwordET.getText().toString())
                .setTag("reset_password")
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
                                showToast(getString(R.string.success));
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
