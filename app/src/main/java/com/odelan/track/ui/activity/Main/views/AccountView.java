package com.odelan.track.ui.activity.Main.views;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.utils.TabAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountView extends BaseView {

    TabAdapter tabAdapter;

    @BindView(R.id.firstTV) TextView firstTab;
    @BindView(R.id.secondTV) TextView secondTab;
    @BindView(R.id.first_tab_content) LinearLayout firstTabContent;
    @BindView(R.id.second_tab_content) LinearLayout secondTabContent;

    @Override
    protected int getLayoutResID() {
        return R.layout.item_account;
    }

    public AccountView(HomeActivity context) {
        super(context);

        initLayout();
    }

    private void initLayout() {
        final ArrayList<TextView> tvs = new ArrayList<TextView>();
        tvs.add(firstTab);
        tvs.add(secondTab);

        final ArrayList<ViewGroup> vgs = new ArrayList<ViewGroup>();
        vgs.add(firstTabContent);
        vgs.add(secondTabContent);

        tabAdapter = new TabAdapter(tvs, vgs);
    }

    @OnClick(R.id.firstTV) public void onFristTabClick() {
        tabAdapter.onTabClick(firstTab);
    }

    @OnClick(R.id.secondTV) public void onSecondTabClick() {
        tabAdapter.onTabClick(secondTab);
    }
}
