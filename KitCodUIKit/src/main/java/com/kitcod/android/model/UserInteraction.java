package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInteraction implements Parcelable {
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("createdTS")
    @Expose
    public String createdTS;
    @SerializedName("updatedTS")
    @Expose
    public String updatedTS;
    @SerializedName("version")
    @Expose
    public Integer version;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("interactionType")
    @Expose
    public String interactionType;
    @SerializedName("resourceType")
    @Expose
    public String resourceType;
    @SerializedName("resourceId")
    @Expose
    public String resourceId;
    @SerializedName("resourceSubType")
    @Expose
    public String resourceSubType;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("active")
    @Expose
    public Boolean active;

    protected UserInteraction(Parcel in) {
        uuid = in.readString();
        createdTS = in.readString();
        updatedTS = in.readString();
        if (in.readByte() == 0) {
            version = null;
        } else {
            version = in.readInt();
        }
        id = in.readString();
        interactionType = in.readString();
        resourceType = in.readString();
        resourceId = in.readString();
        resourceSubType = in.readString();
        userId = in.readString();
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
    }

    public static final Creator<UserInteraction> CREATOR = new Creator<UserInteraction>() {
        @Override
        public UserInteraction createFromParcel(Parcel in) {
            return new UserInteraction(in);
        }

        @Override
        public UserInteraction[] newArray(int size) {
            return new UserInteraction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(createdTS);
        parcel.writeString(updatedTS);
        if (version == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(version);
        }
        parcel.writeString(id);
        parcel.writeString(interactionType);
        parcel.writeString(resourceType);
        parcel.writeString(resourceId);
        parcel.writeString(resourceSubType);
        parcel.writeString(userId);
        if (count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(count);
        }
        parcel.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
    }
}
