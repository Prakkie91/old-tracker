package com.odelan.track.ui.activity.Main.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.data.model.User;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.intro.LoginActivity;
import com.odelan.track.utils.DateTimeUtils;
import com.odelan.track.utils.TabAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

public class AccountView extends BaseView {

    TabAdapter tabAdapter;

    @BindView(R.id.firstTV) TextView firstTab;
    @BindView(R.id.secondTV) TextView secondTab;
    @BindView(R.id.birthDayET) EditText birthDayET;
    @BindView(R.id.firstNameET) EditText firstNameET;
    @BindView(R.id.lastNameET) EditText lastNameET;
    @BindView(R.id.phoneET) EditText phoneET;
    @BindView(R.id.first_tab_content) LinearLayout firstTabContent;
    @BindView(R.id.second_tab_content) LinearLayout secondTabContent;
    @BindView(R.id.adCountTV) TextView adCountTV;
    @BindView(R.id.earnAmountTV) TextView earnAmountTV;


    DateTimeUtils dateTimeUtils;

    String count = "";
    String earnAmount = "";

    @Override
    protected int getLayoutResID() {
        return R.layout.item_account;
    }

    public AccountView(HomeActivity context) {
        super(context);

        //getAccountEarnInfo();
        initLayout();
    }

    public void getAccountEarnInfo() {
        User me = mContext.getMe();
        mContext.showLoading();
        AndroidNetworking.post(SERVER_URL + "order/getAccountEarnInfo")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("user_id", me.userid)
                .setTag("getAccountEarnInfo")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        mContext.dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                JSONObject orderObj = response.getJSONObject("data");
                                count = orderObj.getString("count");
                                earnAmount = orderObj.getString("total_amount");

                                adCountTV.setText(count);
                                earnAmountTV.setText("HKD $" + earnAmount);

                            } else {
                                mContext.showToast(mContext.getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContext.showToast(mContext.getString(R.string.failed));
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        mContext.dismissLoading();
                        mContext.showToast(mContext.getString(R.string.network_error));
                    }
                });
    }

    private void initLayout() {
        final ArrayList<TextView> tvs = new ArrayList<TextView>();
        tvs.add(firstTab);
        tvs.add(secondTab);

        final ArrayList<ViewGroup> vgs = new ArrayList<ViewGroup>();
        vgs.add(firstTabContent);
        vgs.add(secondTabContent);

        tabAdapter = new TabAdapter(tvs, vgs);

        User me = mContext.getMe();
        firstNameET.setText(me.first_name);
        lastNameET.setText(me.last_name);
        birthDayET.setText(me.birthday);
        phoneET.setText(me.phone);
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
        getAccountEarnInfo();
    }

    @OnClick(R.id.saveBtn) public void onSave() {
        User me = mContext.getMe();
        mContext.showLoading();
        AndroidNetworking.post(SERVER_URL + "user/update_profile")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("user_id", me.userid)
                .addBodyParameter("first_name", firstNameET.getText().toString())
                .addBodyParameter("last_name", lastNameET.getText().toString())
                .addBodyParameter("birthday", birthDayET.getText().toString())
                .addBodyParameter("phone", phoneET.getText().toString())
                .setTag("updateProfile")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        mContext.dismissLoading();
                        try {
                            int status = response.getInt("status");
                            if (status == 1) {
                                mContext.showToast(mContext.getString(R.string.success));
                            } else {
                                mContext.showToast(mContext.getString(R.string.failed));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContext.showToast(mContext.getString(R.string.failed));
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        mContext.dismissLoading();
                        mContext.showToast(mContext.getString(R.string.network_error));
                    }
                });
    }

    @OnClick (R.id.logoutBtn) public void onLogout() {

        mContext.saveKeyValue("email", "");
        mContext.saveKeyValue("password", "");
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
