package com.odelan.track.ui.activity.Main.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;

import butterknife.ButterKnife;

public abstract class BaseView {

    public BaseActivity mContext;
    public View mContainerView = null;

    protected abstract int getLayoutResID();

    public BaseView () {}

    public BaseView (BaseActivity context) {
        mContext = context;

        mContainerView = LayoutInflater.from(
                mContext.getBaseContext()).inflate(getLayoutResID(), null, false);

        ButterKnife.bind(this, mContainerView);

    }
}
