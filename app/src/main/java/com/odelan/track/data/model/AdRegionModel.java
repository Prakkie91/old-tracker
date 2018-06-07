package com.odelan.track.data.model;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class AdRegionModel extends BaseModel {

    @JsonField(name = "id")
    public String aid;

    @JsonField(name = "ad_region")
    public String region;

    @JsonField(name = "lat")
    public double lat;

    @JsonField(name = "lng")
    public double lng;

    @JsonField(name = "radius")
    public double radius; // (unit: m)

    @JsonField(name = "order_id")
    public String order_id;
}
