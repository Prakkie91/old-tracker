package com.odelan.track.data.model;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class Area extends BaseModel {

    @JsonField(name = "id")
    public String aid;

    @JsonField(name = "lat")
    public double lat;

    @JsonField(name = "lng")
    public double lng;

    @JsonField(name = "radius")
    public double radius; // (unit: m)

    @JsonField(name = "driver_id")
    public String driver_id; // who is perform task in accepted case

    @JsonField(name = "status")
    public String status; // pending, accepted, completed
}
