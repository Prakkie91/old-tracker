package com.odelan.track.ui.activity.Main.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.odelan.track.ui.activity.Main.OrderDetailActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.odelan.track.MyApplication.SERVER_URL;
import static com.odelan.track.MyApplication.X_API_KEY;

public class OrdersView extends BaseView {

    public List<Order> mData = new ArrayList<>();

    final int COLUMN_COUNT = 1;
    final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResID() {
        return R.layout.item_orders;
    }

    public OrdersView(HomeActivity context) {
        super(context);

        setLayout();
    }

    public void getAllOrders () {
        mContext.showLoading();
        AndroidNetworking.post(SERVER_URL + "order/getAllOrders")
                .addHeaders("X-API-KEY", X_API_KEY)
                .setTag("getAllOrders")
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
                                mData = LoganSquare.parseList(response.getString("data"), Order.class);
                                recyclerView.setAdapter(new RecyclerViewAdapter(mData));
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

    private void setLayout() {
        RecyclerView.LayoutManager layoutManager;

        if (GRID_LAYOUT) {
            layoutManager = new GridLayoutManager(mContext, COLUMN_COUNT);
        } else {
            layoutManager = new LinearLayoutManager(mContext);
        }

        recyclerView.setLayoutManager(layoutManager);
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<Order> mList;

        public RecyclerViewAdapter(List<Order> list) {
            mList = list;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_orders, parent, false);
            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder mholder, final int position) {
            final RecyclerViewAdapter.ViewHolder holder = (RecyclerViewAdapter.ViewHolder) mholder;
            holder.mItem = mList.get(position);
            String desc = mContext.getString(R.string.order) + holder.mItem.oid + ", " + holder.mItem.car_type + ", " + holder.mItem.amount;
            holder.tv.setText(desc);

            User me = mContext.getMe();
            if (holder.mItem.status.equals(Order.STATUS_ACCEPTED) && me.userid.equals(holder.mItem.driver_id)) {
                holder.cv.setCardBackgroundColor(Color.parseColor("#880000ff"));
            } else {
                holder.cv.setCardBackgroundColor(Color.WHITE);
            }

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                    OrderDetailActivity.mOrderId = holder.mItem.oid;
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView tv;
            public final CardView cv;

            public Order mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                tv = view.findViewById(R.id.tv);
                cv = view.findViewById(R.id.card_view);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }
    }
}
