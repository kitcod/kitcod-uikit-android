package com.kitcod.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InteractionSummary implements Serializable {

    @SerializedName("interactionCounts")
    @Expose
    public InteractionCounts interactionCounts;
    @SerializedName("userInteractions")
    @Expose
    public List<UserInteraction> userInteractions = null;
}
