package com.odelan.track.ui.activity.intro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.odelan.track.R;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.DateTimeUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverLicenseActivity extends BaseActivity {

    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.expireDateET)
    EditText expireDateET;

    @BindView(R.id.classET)
    EditText classET;

    DateTimeUtils dateTimeUtils;



    @Override
    protected int getLayoutResID() {
        return R.layout.activity_driver_license;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateTimeUtils = new DateTimeUtils("dd/MM/yyyy");
    }

    @OnClick(R.id.nextBtn) public void onNext() {
        if (numberET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_license_number));
            return;
        }

        if (expireDateET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_expiry_date));
            return;
        }

        if (classET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_license_class));
            return;
        }

        Intent i = getIntent();
        String first_name = i.getExtras().getString("first_name");
        String last_name = i.getExtras().getString("last_name");
        String email = i.getExtras().getString("email");
        String birthday = i.getExtras().getString("birthday");
        String phone = i.getExtras().getString("phone");
        String password = i.getExtras().getString("password");

        Intent intent = new Intent(mContext, VehicleActivity.class);
        intent.putExtra("first_name", first_name);
        intent.putExtra("last_name", last_name);
        intent.putExtra("email", email);
        intent.putExtra("birthday", birthday);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("driving_license_number", numberET.getText().toString());
        intent.putExtra("expire_date", expireDateET.getText().toString());
        intent.putExtra("license_class", classET.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.calendarIV) public void onCalender() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog mDatePicker = new android.app.DatePickerDialog (
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
        );

        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
        mDatePicker.show();
    }
}
