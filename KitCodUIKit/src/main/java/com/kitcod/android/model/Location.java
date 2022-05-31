package com.kitcod.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("geoCoordinates")
    @Expose
    public GeoCoordinates geoCoordinates;
    @SerializedName("address")
    @Expose
    public Address address;
    @SerializedName("gplaceId")
    @Expose
    public String gplaceId;
    @SerializedName("active")
    @Expose
    public Boolean active;
}
