package com.kitcod.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Media {
    @SerializedName("docs")
    @Expose
    public List<Doc> docs = null;
    @SerializedName("auds")
    @Expose
    public List<Doc> auds = null;
    @SerializedName("imgs")
    @Expose
    public List<Doc> imgs = null;

    @SerializedName("proImg")
    @Expose
    public Doc proImg = null;
    @SerializedName("covImg")
    @Expose
    public Doc covImg = null;
}
