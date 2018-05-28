package com.odelan.track.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.track.utils.Common;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 7/18/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnLocaleChangedListener {
    public BaseActivity mContext;
    public String TAG = "BaseActivity";

    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    protected abstract int getLayoutResID();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());

        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        localizationDelegate.onResume(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(localizationDelegate.attachBaseContext(newBase));
    }

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    public final void setLanguage(String language) {
        localizationDelegate.setLanguage(this, language);
    }

    public final void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }

    public final void setDefaultLanguage(String language) {
        localizationDelegate.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        localizationDelegate.setDefaultLanguage(locale);
    }

    public final Locale getCurrentLanguage() {
        return localizationDelegate.getLanguage(this);
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
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
