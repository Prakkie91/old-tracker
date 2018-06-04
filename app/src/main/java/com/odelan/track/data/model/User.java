package com.odelan.track.data.model;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Administrator on 11/21/2016.
 */

@JsonObject
public class User extends BaseModel {

    @JsonField(name = "id")
    public String userid;

    @JsonField(name = "username")
    public String username;

    @JsonField(name = "email")
    public String email;

    @JsonField(name = "first_name")
    public String first_name;

    @JsonField(name = "last_name")
    public String last_name;

    @JsonField(name = "birthday")
    public String birthday;

    @JsonField(name = "driving_license_number")
    public String driving_license_number;

    @JsonField(name = "driving_license_expiry_date")
    public String driving_license_expiry_date;

    @JsonField(name = "driving_license_class")
    public String driving_license_class;

    @JsonField(name = "car_type_id")
    public String car_type_id;

    @JsonField(name = "car_type")
    public String car_type;

    @JsonField(name = "vehicle_plate_number")
    public String vehicle_plate_number;

    @JsonField(name = "plate_number_expiry_date")
    public String plate_number_expiry_date;

    @JsonField(name = "photo_vehicle")
    public String photo_vehicle;

    @JsonField(name = "photo_plate_number")
    public String photo_plate_number;

    @JsonField(name = "phone")
    public String phone;
}
