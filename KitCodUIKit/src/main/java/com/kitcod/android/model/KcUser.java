package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KcUser implements Parcelable {

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
    @SerializedName("aboutMe")
    public String aboutMe;
    @SerializedName("media")
    @Expose
    public Media media;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("yob")
    @Expose
    public Integer yob;
    @SerializedName("homeLocation")
    @Expose
    public Location homeLocation;
    @SerializedName("contactPoints")
    @Expose
    public List<ContactPoint> contactPoints = null;
    @SerializedName("premiumUntilTS")
    @Expose
    public String premiumUntilTS;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("userCommunities")
    @Expose
    public List<KcGroup> userCommunities = null;
    @SerializedName("active")
    @Expose
    public Boolean active;

    protected KcUser(Parcel in) {
        uuid = in.readString();
        createdTS = in.readString();
        updatedTS = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        if (in.readByte() == 0) {
            version = null;
        } else {
            version = in.readInt();
        }
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        if (in.readByte() == 0) {
            yob = null;
        } else {
            yob = in.readInt();
        }
        premiumUntilTS = in.readString();
        type = in.readString();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
    }

    public static final Creator<KcUser> CREATOR = new Creator<KcUser>() {
        @Override
        public KcUser createFromParcel(Parcel in) {
            return new KcUser(in);
        }

        @Override
        public KcUser[] newArray(int size) {
            return new KcUser[size];
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
        parcel.writeString(createdBy);
        parcel.writeString(updatedBy);
        if (version == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(version);
        }
        parcel.writeString(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        if (gender == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(gender);
        }
        if (yob == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(yob);
        }
        parcel.writeString(premiumUntilTS);
        parcel.writeString(type);
        parcel.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
    }
}
