package com.odelan.track.ui.activity.Main.views;

import android.view.View;

import com.odelan.track.ui.base.BaseActivity;

public class BaseView {

    public BaseActivity mContext;
    public View mContainerView = null;

    public BaseView () {}

    public BaseView (BaseActivity context) {
        mContext = context;
    }
}
