package com.kitcod.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class KcGroup implements Serializable {
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("createdTS")
    @Expose
    public String createdTS;
    @SerializedName("updatedTS")
    @Expose
    public String updatedTS;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("updatedBy")
    @Expose
    public String updatedBy;
    @SerializedName("version")
    @Expose
    public Integer version;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("accountId")
    @Expose
    public Integer accountId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("handle")
    @Expose
    public String handle;
    @SerializedName("communityType")
    @Expose
    public String communityType;
    @SerializedName("location")
    @Expose
    public Location location;
    @SerializedName("webSiteUrl")
    @Expose
    public String webSiteUrl;
    @SerializedName("contactPoints")
    @Expose
    public List<ContactPoint> contactPoints = null;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("creationSourceType")
    @Expose
    public String creationSourceType;
    @SerializedName("contentVisibilityType")
    @Expose
    public String contentVisibilityType;
    @SerializedName("businessCategory")
    @Expose
    public String businessCategory;
    /*  @SerializedName("settings")
      @Expose
      public List<Setting> settings = null;*/
    @SerializedName("media")
    @Expose
    public Media media;
    /* @SerializedName("socialConnections")
     @Expose
     public List<SocialConnection> socialConnections = null;
     @SerializedName("tags")
     @Expose
     public List<Tag> tags = null;
     @SerializedName("metadata")
     @Expose
     public Metadata metadata;
     @SerializedName("eligibility")
     @Expose
     public Eligibility eligibility;*/
    @SerializedName("registered")
    @Expose
    public Boolean registered;
    @SerializedName("business")
    @Expose
    public Boolean business;
    @SerializedName("premium")
    @Expose
    public Boolean premium;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("verified")
    @Expose
    public Boolean verified;
}
