package com.kitcod.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KcGroupsBulk implements Serializable {
    @SerializedName("uuid")
    public String uuid;
    @SerializedName("id")
    public String id;
    @SerializedName("httpMethod")
    public String httpMethod;
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("body")
    public KcGroup body;
    @SerializedName("active")
    public Boolean active;
}
