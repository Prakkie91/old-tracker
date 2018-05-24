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

import com.odelan.track.MyApplication;
import com.odelan.track.R;
import com.odelan.track.data.model.Order;
import com.odelan.track.ui.activity.Main.CurrentOrderDetailActivity;
import com.odelan.track.ui.activity.Main.HomeActivity;
import com.odelan.track.ui.activity.Main.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        getData();
    }

    public void getData() {
        mData = new ArrayList<>();

        Order item = new Order();
        item.oid = "1";
        item.title = "Order1";
        item.lat = MyApplication.g_latitude - 0.1;
        item.lng = MyApplication.g_longitude - 0.1;
        item.radius = 5000;
        item.status = Order.STATUS_ACCEPTED;
        mData.add(item);

        item = new Order();
        item.oid = "2";
        item.title = "Order2";
        item.lat = MyApplication.g_latitude -0.1515;
        item.lng = MyApplication.g_longitude + 0.1212;
        item.radius = 10000;
        item.status = Order.STATUS_PENDING;
        mData.add(item);

        item = new Order();
        item.oid = "3";
        item.title = "Order3";
        item.lat = MyApplication.g_latitude + 0.3;
        item.lng = MyApplication.g_longitude - 0.3;
        item.radius = 8000;
        item.status = Order.STATUS_PENDING;
        mData.add(item);

        recyclerView.setAdapter(new RecyclerViewAdapter(mData));
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
            holder.tv.setText(holder.mItem.title);

            if (holder.mItem.status == Order.STATUS_ACCEPTED) {
                holder.cv.setCardBackgroundColor(Color.parseColor("#880000ff"));
            } else {
                holder.cv.setCardBackgroundColor(Color.WHITE);
            }

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mItem.status == Order.STATUS_ACCEPTED) {
                        mContext.startActivity(new Intent(mContext, CurrentOrderDetailActivity.class));
                        CurrentOrderDetailActivity.mOrder = holder.mItem;
                    } else {
                        mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
                        OrderDetailActivity.mOrder = holder.mItem;
                    }
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
