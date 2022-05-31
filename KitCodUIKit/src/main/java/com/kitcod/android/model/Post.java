package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable, Cloneable {
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
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("contentHTML")
    @Expose
    public String contentHTML;
    @SerializedName("contentMarkdown")
    @Expose
    public String contentMarkdown;
    @SerializedName("postType")
    @Expose
    public String postType;
    @SerializedName("communityId")
    @Expose
    public String communityId;
    @SerializedName("publishedTS")
    @Expose
    public String publishedTS;
    @SerializedName("interactionSummary")
    @Expose
    public InteractionSummary interactionSummary;
    @SerializedName("communityContentVisibility")
    @Expose
    public String communityContentVisibility;
    @SerializedName("media")
    @Expose
    public Media media;
    public KcGroup group;
    @SerializedName("businessCommunity")
    @Expose
    public Boolean businessCommunity;
    @SerializedName("active")
    @Expose
    public Boolean active;
    @SerializedName("poll")
    @Expose
    public Poll poll;
    @SerializedName("edited")
    @Expose
    public Boolean edited;
    public boolean isPlaying;
    public boolean isVoted = false;

    protected Post(Parcel in) {
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
        title = in.readString();
        contentHTML = in.readString();
        contentMarkdown = in.readString();
        postType = in.readString();
        communityId = in.readString();
        publishedTS = in.readString();
        communityContentVisibility = in.readString();
        byte tmpBusinessCommunity = in.readByte();
        businessCommunity = tmpBusinessCommunity == 0 ? null : tmpBusinessCommunity == 1;
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        poll = in.readParcelable(Poll.class.getClassLoader());
        byte tmpEdited = in.readByte();
        edited = tmpEdited == 0 ? null : tmpEdited == 1;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
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
        parcel.writeString(title);
        parcel.writeString(contentHTML);
        parcel.writeString(contentMarkdown);
        parcel.writeString(postType);
        parcel.writeString(communityId);
        parcel.writeString(publishedTS);
        parcel.writeString(communityContentVisibility);
        parcel.writeByte((byte) (businessCommunity == null ? 0 : businessCommunity ? 1 : 2));
        parcel.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        parcel.writeParcelable(poll, i);
        parcel.writeByte((byte) (edited == null ? 0 : edited ? 1 : 2));
    }

    public ExoplayerModel getExoplayerModel() {
        return exoplayerModel;
    }

    public void setExoplayerModel(ExoplayerModel exoplayerModel) {
        this.exoplayerModel = exoplayerModel;
    }

    public transient ExoplayerModel exoplayerModel;

    @Override
    public Post clone() {

        Post clone;
        try {
            clone = (Post) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return clone;
    }
}
