package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doc implements Parcelable {
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("resourceType")
    @Expose
    public String resourceType;
    @SerializedName("resourceId")
    @Expose
    public String resourceId;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("mediaUrl")
    @Expose
    public String mediaUrl;
    @SerializedName("contentType")
    @Expose
    public String contentType;
    @SerializedName("active")
    @Expose
    public Boolean active;
    public boolean isPlaying;
    @SerializedName("resizedMediaList")
    public List<ResizedMediaList> resizedMediaList = null;


    protected Doc(Parcel in) {
        uuid = in.readString();
        id = in.readString();
        resourceType = in.readString();
        resourceId = in.readString();
        type = in.readString();
        mediaUrl = in.readString();
        contentType = in.readString();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        isPlaying = in.readByte() != 0;
    }

    public static final Creator<Doc> CREATOR = new Creator<Doc>() {
        @Override
        public Doc createFromParcel(Parcel in) {
            return new Doc(in);
        }

        @Override
        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(id);
        parcel.writeString(resourceType);
        parcel.writeString(resourceId);
        parcel.writeString(type);
        parcel.writeString(mediaUrl);
        parcel.writeString(contentType);
        parcel.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        parcel.writeByte((byte) (isPlaying ? 1 : 0));
    }
}
