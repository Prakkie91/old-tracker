package com.odelan.track.ui.activity.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bluelinelabs.logansquare.LoganSquare;
import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.AdRegionModel;
import com.odelan.track.data.model.Order;
import com.odelan.track.data.model.User;
import com.odelan.track.ui.activity.Main.views.OrdersView;
import com.odelan.track.ui.base.BaseActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

public class OrderDetailActivity extends BaseActivity {

    public static String mOrderId = null;

    @BindView(R.id.titleTV)
    TextView titleTV;

    @BindView(R.id.regionTV)
    TextView regionTV;

    @BindView(R.id.carTypeTV)
    TextView carTypeTV;

    @BindView(R.id.adTypeTV)
    TextView adTypeTV;

    @BindView(R.id.adThemeTV)
    TextView adThemeTV;

    @BindView(R.id.periodTV)
    TextView periodTV;

    @BindView(R.id.adsizeTV)
    TextView adsizeTV;

    @BindView(R.id.amountTV)
    TextView amountTV;

    @BindView(R.id.statusTV)
    TextView statusTV;

    @BindView(R.id.cancelBtn)
    Button cancelBtn;

    @BindView(R.id.acceptBtn)
    Button acceptBtn;

    @BindView(R.id.completeBtn)
    Button completeBtn;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText(getString(R.string.order) + mOrderId);
        getOrder();
    }

    public void getOrder() {
        showLoading();
        AndroidNetworking.post(SERVER_URL + "order/getOrderWithID")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("order_id", mOrderId)
                .setTag("getOrderWithID")
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
                                Order order = LoganSquare.parse(orderObj.toString(), Order.class);
                                showlayout(order);
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

    void showlayout(Order o) {
        String adRegions = "";
        for (AdRegionModel am : o.ad_regions) {
            adRegions += am.region + ", ";
        }

        regionTV.setText(adRegions.substring(0, adRegions.length()-2));
        carTypeTV.setText(getCarTypeWithID(o.car_type_id));
        adTypeTV.setText(getAdTypeWithID(o.ad_type_id));
        adThemeTV.setText(o.ad_theme);
        periodTV.setText(o.ad_period);
        adsizeTV.setText(o.ad_size);
        amountTV.setText(o.amount);

        if (o.status.equals(Order.STATUS_PENDING)) {
            statusTV.setText(getStatusStr(o.status));
            acceptBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);
            completeBtn.setVisibility(View.GONE);
        }

        if (o.status.equals(Order.STATUS_ACCEPTED)) {
            User me = getMe();
            if (me.userid.equals(o.driver_id)) {
                statusTV.setText(getStatusStr(o.status) + " by me");
                acceptBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                completeBtn.setVisibility(View.VISIBLE);
            } else {
                statusTV.setText(getStatusStr(o.status) + " by other");
                acceptBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                completeBtn.setVisibility(View.GONE);
            }
        }

        if (o.status.equals(Order.STATUS_COMPLETED) || o.status.equals(Order.STATUS_DELETED)) {
            statusTV.setText(getStatusStr(o.status));
            acceptBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            completeBtn.setVisibility(View.GONE);
        }
    }

    private String getStatusStr(String s) {
        String status = "";
        switch (s) {
            case Order.STATUS_PENDING:
                status = getString(R.string.pending);
                break;
            case Order.STATUS_ACCEPTED:
                status = getString(R.string.accepted);
                break;
            case Order.STATUS_COMPLETED:
                status = getString(R.string.completed);
                break;
            case Order.STATUS_DELETED:
                status = getString(R.string.deleted);
                break;

        }
        return status;
    }

    private String getCarTypeWithID(String cid) {
        String carType = "";
        switch (cid) {
            case "1":
                carType = getString(R.string.van);
                break;
            case "2":
                carType = getString(R.string.five_truck);
                break;
            case "3":
                carType = getString(R.string.nine_truck);
                break;
            case "4":
                carType = getString(R.string.tour_bus);
                break;
            case "5":
                carType = getString(R.string.private_car);
                break;
        }
        return carType;
    }

    private String getAdTypeWithID(String aid) {
        String adType = "";
        switch (aid) {
            case "1":
                adType = getString(R.string.full_body);
                break;
            case "2":
                adType = getString(R.string.twice_sided);
                break;
            case "3":
                adType = getString(R.string.back_door);
                break;
        }
        return adType;
    }

    @OnClick (R.id.backIV) public void onBack() {
        finish();
    }

    @OnClick (R.id.acceptBtn) public void onAccept() {
        updateStatus(Order.STATUS_ACCEPTED);
    }

    @OnClick (R.id.cancelBtn) public void onCancel() {
        updateStatus(Order.STATUS_PENDING);
    }

    @OnClick (R.id.completeBtn) public void onComplete() {
        updateStatus(Order.STATUS_COMPLETED);
    }

    public void updateStatus(final String state) {
        User me = getMe();
        showLoading();
        AndroidNetworking.post(SERVER_URL + "order/updateOrderStatus")
                .addHeaders("X-API-KEY", X_API_KEY)
                .addBodyParameter("order_id", mOrderId)
                .addBodyParameter("user_id", me.userid)
                .addBodyParameter("status", state)
                .setTag("updateOrderStatus")
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
                                Order order = LoganSquare.parse(orderObj.toString(), Order.class);
                                showlayout(order);

                                if (state.equals(Order.STATUS_PENDING) || state.equals(Order.STATUS_COMPLETED)) {
                                    //MyApplication.stopGPSservice(mContext);
                                }
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
}
