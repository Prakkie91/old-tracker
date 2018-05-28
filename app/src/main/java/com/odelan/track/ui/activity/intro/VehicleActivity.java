package com.odelan.track.ui.activity.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.odelan.track.R;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.DateTimeUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleActivity extends BaseActivity {

    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.expireDateET)
    EditText expireDateET;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.areaET)
    EditText areaET;

    @BindView(R.id.vehicleIV)
    ImageView vehicleIV;

    @BindView(R.id.vehicleNumberIV)
    ImageView vehicleNumberIV;

    DateTimeUtils dateTimeUtils;
    List<String>vtypes = new ArrayList<>();

    final String VEHICLE_PHOTO = "vehicle_photo";
    final String VEHICLE_NUMBER_PHOTO = "vehicle_number_photo";
    String photoType = "";

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_vehicle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateTimeUtils = new DateTimeUtils("dd/MM/yyyy");

        vtypes = new ArrayList<>();
        vtypes.add(getString(R.string.van));
        vtypes.add(getString(R.string.five_truck));
        vtypes.add(getString(R.string.nine_truck));
        vtypes.add(getString(R.string.tour_bus));
        vtypes.add(getString(R.string.private_car));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, vtypes);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
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

    @OnClick(R.id.vehicleIV) public void onVehicleIV() {
        photoType = VEHICLE_PHOTO;
        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @OnClick(R.id.vehicleNumberIV) public void onvehicleNumberIV() {
        photoType = VEHICLE_NUMBER_PHOTO;
        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (photoType.equals(VEHICLE_PHOTO)) {
                    vehicleIV.setImageURI(result.getUri());
                } else if (photoType.equals(VEHICLE_NUMBER_PHOTO)) {
                    vehicleNumberIV.setImageURI(result.getUri());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showToast("Cropping failed: " + result.getError());
            }
        }
    }
}
