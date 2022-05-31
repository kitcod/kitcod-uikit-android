package com.kitcod.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("addressLine1")
    @Expose
    public String addressLine1;
    @SerializedName("displayAddressLine")
    @Expose
    public String displayAddressLine;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    @SerializedName("active")
    @Expose
    public Boolean active;
}
