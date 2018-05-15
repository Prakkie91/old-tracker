package com.odelan.track.ui.activity.Main.views;

import android.view.LayoutInflater;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;

import butterknife.ButterKnife;

public class SettingsView extends BaseView {

    public SettingsView(HomeActivity context) {
        super(context);

        mContainerView = LayoutInflater.from(
                mContext.getBaseContext()).inflate(R.layout.item_settings, null, false);

        ButterKnife.bind(this, mContainerView);
    }
}
