package com.odelan.track.data.model;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class Order extends BaseModel {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_DELETED = "deleted";

    @JsonField(name = "order_id")
    public String oid;

    @JsonField(name = "ad_ids")
    public String aids;

    @JsonField(name = "ad_regions")
    public List<AdRegionModel> ad_regions;

    @JsonField(name = "car_type_id")
    public String car_type_id;

    @JsonField(name = "car_type")
    public String car_type;

    @JsonField(name = "ad_theme")
    public String ad_theme;

    @JsonField(name = "ad_type_id")
    public String ad_type_id;

    @JsonField(name = "ad_type")
    public String ad_type;

    @JsonField(name = "ad_size")
    public String ad_size;

    @JsonField(name = "ad_period")
    public String ad_period;

    @JsonField(name = "amount")
    public String amount;

    @JsonField(name = "status")
    public String status;

    @JsonField(name = "user_id")
    public String driver_id;

    @JsonField(name = "created_at")
    public String created_at;

    @JsonField(name = "start_date")
    public String start_date;

    @JsonField(name = "end_date")
    public String end_date;

    @JsonField(name = "order_days")
    public String order_days;

    @JsonField(name = "order_months")
    public String order_months;

    @JsonField(name = "isPassed")
    public String isPassed;
}
