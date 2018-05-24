package com.odelan.track.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.track.utils.Common;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 7/18/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity mContext;
    public String TAG = "BaseActivity";

    protected abstract int getLayoutResID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void saveKeyValue(String key, String value) {
        Common.saveInfoWithKeyValue(mContext, key, value);
    }

    public String getValueFromKey(String key) {
        return Common.getInfoWithValueKey(mContext, key);
    }

    private Toast mToast;

    public void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private KProgressHUD kProgressHUD = null;
    public void showLoading() {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait...")
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        }

        kProgressHUD.show();
    }

    public void showLoading(String message) {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setWindowColor(Color.parseColor("#DDDDDDDD"))
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f);
        } else {
            kProgressHUD.setLabel(message);
        }

        kProgressHUD.show();
    }

    public void dismissLoading() {
        if(kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    /*@Override
    public void onBackPressed() {
        finish();
    }*/
}
