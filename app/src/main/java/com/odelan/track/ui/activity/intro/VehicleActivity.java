package com.odelan.track.ui.activity.intro;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.R;
import com.odelan.track.data.model.User;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.base.BaseActivity;
import com.odelan.track.utils.Common;
import com.odelan.track.utils.DateTimeUtils;
import com.odelan.track.utils.FileUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;
import static com.odelan.track.MyApplication.g_NumberPhoto;
import static com.odelan.track.MyApplication.g_VehiclePhoto;

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
    String carType = "";

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

        SpinnerAdapter dataAdapter = new SpinnerAdapter(this, R.layout.spinner_item);
        dataAdapter.addAll(vtypes);
        dataAdapter.add(getString(R.string.vehicle_type)); // for hint text
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(dataAdapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem() == getString(R.string.vehicle_type)) {

                } else {
                    carType = vtypes.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        g_VehiclePhoto = null;
        g_NumberPhoto = null;
    }

    @OnClick(R.id.nextBtn) public void onNext() {
        if (carType.isEmpty()) {
            showToast(getString(R.string.warning_car_type));
            return;
        }

        if (numberET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_plate_number));
            return;
        }

        if (expireDateET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_plate_number_expiry_date));
            return;
        }

        if (areaET.getText().toString().isEmpty()) {
            showToast(getString(R.string.warning_ad_area));
            return;
        }

        if (g_VehiclePhoto == null) {
            showToast(getString(R.string.warning_vehicle_photo));
            return;
        }

        if (g_NumberPhoto == null) {
            showToast(getString(R.string.warning_plate_number_photo));
            return;
        }

        Intent i = getIntent();
        String first_name = i.getExtras().getString("first_name");
        String last_name = i.getExtras().getString("last_name");
        String email = i.getExtras().getString("email");
        String birthday = i.getExtras().getString("birthday");
        String phone = i.getExtras().getString("phone");
        String password = i.getExtras().getString("password");
        String driving_license_number = i.getExtras().getString("driving_license_number");
        String expire_date = i.getExtras().getString("expire_date");
        String license_class = i.getExtras().getString("license_class");

        if (g_VehiclePhoto.length() == 0) {
            showToast("vhehicle photo file size is 0");
            return;
        }

        if (g_NumberPhoto.length() == 0) {
            showToast("plate number photo file size is 0");
            return;
        }

        showLoading();
        AndroidNetworking.upload(SERVER_URL + "user/signup")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addMultipartFile("photo_vehicle", g_VehiclePhoto)
                .addMultipartFile("photo_plate_number", g_NumberPhoto)
                .addMultipartParameter("first_name", first_name)
                .addMultipartParameter("last_name", last_name)
                .addMultipartParameter("email", email)
                .addMultipartParameter("birthday", birthday)
                .addMultipartParameter("phone", phone)
                .addMultipartParameter("password", password)
                .addMultipartParameter("driving_license_number", driving_license_number)
                .addMultipartParameter("driving_license_expiry_date", expire_date)
                .addMultipartParameter("driving_license_class", license_class)
                .addMultipartParameter("car_type", carType)
                .addMultipartParameter("vehicle_plate_number", numberET.getText().toString())
                .addMultipartParameter("plate_number_expiry_date", expireDateET.getText().toString())
                .setPriority(Priority.LOW)
                .setTag("Signup")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONObject user = response.getJSONObject("data");
                                User me = LoganSquare.parse(user.toString(), User.class);
                                saveKeyValue("user", user.toString());
                                startActivity(new Intent(mContext, HomeActivity.class));
                            } else {
                                showToast(getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast(getString(R.string.failed));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        dismissLoading();
                        showToast(getString(R.string.network_error));
                    }
                });
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
                    g_VehiclePhoto = new File(FileUtils.saveImageToInternalStorageFromUri(mContext, result.getUri()));
                } else if (photoType.equals(VEHICLE_NUMBER_PHOTO)) {
                    vehicleNumberIV.setImageURI(result.getUri());
                    g_NumberPhoto = new File(FileUtils.saveImageToInternalStorageFromUri(mContext, result.getUri()));
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showToast("Cropping failed: " + result.getError());
            }
        }
    }

    class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {

            // TODO Auto-generated method stub
            int count = super.getCount();

            return count>0 ? count-1 : count ;
        }
    }
}
