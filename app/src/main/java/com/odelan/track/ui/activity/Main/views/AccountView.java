package com.odelan.track.ui.activity.Main.views;

import android.app.DatePickerDialog;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.utils.DateTimeUtils;
import com.odelan.track.utils.TabAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountView extends BaseView {

    TabAdapter tabAdapter;

    @BindView(R.id.firstTV) TextView firstTab;
    @BindView(R.id.secondTV) TextView secondTab;
    @BindView(R.id.birthDayET) EditText birthDayET;
    @BindView(R.id.first_tab_content) LinearLayout firstTabContent;
    @BindView(R.id.second_tab_content) LinearLayout secondTabContent;

    DateTimeUtils dateTimeUtils;

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

    @OnClick(R.id.birthDayET) public void onBirthDay() {
        // at least 18 years old

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.YEAR, -18);
        Date newdate = now.getTime();

        DatePickerDialog mDatePicker = new android.app.DatePickerDialog(
                mContext,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int m = month + 1;
                        String monthStr = String.valueOf(m);
                        if (m < 9) {
                            monthStr = "0" + m;
                        }

                        String date = dayOfMonth + "/" + monthStr + "/" + year;
                        birthDayET.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        mDatePicker.getDatePicker().setMaxDate(newdate.getTime());
        mDatePicker.show();
    }

    @OnClick(R.id.firstTV) public void onFristTabClick() {
        tabAdapter.onTabClick(firstTab);
    }

    @OnClick(R.id.secondTV) public void onSecondTabClick() {
        tabAdapter.onTabClick(secondTab);
    }
}
