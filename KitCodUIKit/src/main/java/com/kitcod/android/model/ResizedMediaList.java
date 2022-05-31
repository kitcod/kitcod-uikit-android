package com.kitcod.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 5/7/18.
 */

public class ResizedMediaList implements Serializable {

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("type")
    public String type;

    @SerializedName("bucketName")
    public String bucketName;

    @SerializedName("keyName")
    public String keyName;

    @SerializedName("etag")
    public String etag;

    @SerializedName("mediaUrl")
    public String mediaUrl;

    @SerializedName("active")
    public Boolean active;

    private final static long serialVersionUID = -3279585437178615910L;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
