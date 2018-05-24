package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.DateTimeUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleActivity extends BaseActivity {

    @BindView(R.id.typeET)
    EditText typeET;

    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.expireDateET)
    EditText expireDateET;

    DateTimeUtils dateTimeUtils;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_vehicle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateTimeUtils = new DateTimeUtils("dd/MM/yyyy");
    }

    @OnClick(R.id.nextBtn) public void onNext() {
        startActivity(new Intent(mContext, HomeActivity.class));
    }

    @OnClick(R.id.calendarIV) public void onCalender() {
        Calendar now = Calendar.getInstance();
        new android.app.DatePickerDialog(
                this,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int m = month + 1;
                        String monthStr = String.valueOf(m);
                        if (m < 9) {
                            monthStr = "0" + m;
                        }

                        String date = dayOfMonth + "/" + monthStr + "/" + year;
                        expireDateET.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
