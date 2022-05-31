package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentModel implements Parcelable {
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
    @SerializedName("resourceType")
    @Expose
    public String resourceType;
    @SerializedName("resourceId")
    @Expose
    public String resourceId;
    @SerializedName("userId")
    @Expose
    public String userId;
    @SerializedName("communityId")
    @Expose
    public String communityId;
    @SerializedName("html")
    @Expose
    public String html;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("edited")
    @Expose
    public Boolean edited;
    @SerializedName("interactionSummary")
    @Expose
    public InteractionSummary interactionSummary;

    protected CommentModel(Parcel in) {
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
        resourceType = in.readString();
        resourceId = in.readString();
        userId = in.readString();
        communityId = in.readString();
        html = in.readString();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        byte tmpEdited = in.readByte();
        edited = tmpEdited == 0 ? null : tmpEdited == 1;
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
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
        parcel.writeString(resourceType);
        parcel.writeString(resourceId);
        parcel.writeString(userId);
        parcel.writeString(communityId);
        parcel.writeString(html);
        parcel.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        parcel.writeByte((byte) (edited == null ? 0 : edited ? 1 : 2));
    }
}
