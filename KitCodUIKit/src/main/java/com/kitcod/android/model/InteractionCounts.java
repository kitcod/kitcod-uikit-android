package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InteractionCounts implements Serializable {

    @SerializedName("LIKEOPTION1")
    @Expose
    public int likeoption1;
    @SerializedName("VIEWTOT")
    @Expose
    public int viewtot;
    @SerializedName("VIEW")
    @Expose
    public int view;
    @SerializedName("LIKEOPTION2")
    @Expose
    public int likeoption2;
    @SerializedName("LIKEOPTION3")
    @Expose
    public int likeoption3;
    @SerializedName("COMMENTTOT")
    @Expose
    public int commenttot;
    @SerializedName("COMMENT")
    @Expose
    public int comment;
    @SerializedName("POLLOPTION2")
    @Expose
    public int polloption2;
    @SerializedName("POLLOPTION3")
    @Expose
    public int polloption3;
    @SerializedName("POLLOPTION1")
    @Expose
    public int polloption1;
    @SerializedName("POLLTOT")
    @Expose
    public int polltot;
    @SerializedName("LIKEOPTION4")
    @Expose
    public int likeoption4;
    @SerializedName("POLLOPTION4")
    @Expose
    public int polloption4;
    @SerializedName("POLLOPTION5")
    @Expose
    public int polloption5;
    @SerializedName("LIKEOPTION5")
    @Expose
    public int likeoption5;
    @SerializedName("LIKEOPTION6")
    @Expose
    public int likeoption6;


}
