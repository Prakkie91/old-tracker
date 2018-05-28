package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.DateTimeUtils;

import java.sql.Driver;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.firstNameET)
    EditText firstNameET;

    @BindView(R.id.lastNameET)
    EditText lastNameET;

    @BindView(R.id.birthDayET)
    EditText birthDayET;

    @BindView(R.id.phoneET)
    EditText phoneET;

    @BindView(R.id.passwordET)
    EditText passworkET;

    DateTimeUtils dateTimeUtils;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_signup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateTimeUtils = new DateTimeUtils("dd/MM/yyyy");
    }

    @OnClick(R.id.signupBtn) public void onSignup() {
        startActivity(new Intent(mContext, DriverLicenseActivity.class));
    }

    @OnClick (R.id.loginTV) public void onLogin() {
        finish();
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
                        birthDayET.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
