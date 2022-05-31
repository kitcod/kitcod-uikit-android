package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded implements Parcelable {

    @SerializedName("postList")
    public List<Post> postList = null;

    @SerializedName("commentList")
    public List<CommentModel> commentList = null;

    @SerializedName("communityList")
    public List<KcGroup> groupList = null;

    protected Embedded(Parcel in) {
        postList = in.createTypedArrayList(Post.CREATOR);
    }

    public static final Creator<Embedded> CREATOR = new Creator<Embedded>() {
        @Override
        public Embedded createFromParcel(Parcel in) {
            return new Embedded(in);
        }

        @Override
        public Embedded[] newArray(int size) {
            return new Embedded[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(postList);
    }
}
