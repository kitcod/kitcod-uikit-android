package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Poll implements Parcelable {
    @SerializedName("options")
    @Expose
    public List<Option> options = null;
    @SerializedName("endDateTime")
    public String pollEndsDateTime;

    protected Poll(Parcel in) {
        options = in.createTypedArrayList(Option.CREATOR);
        pollEndsDateTime = in.readString();
    }

    public static final Creator<Poll> CREATOR = new Creator<Poll>() {
        @Override
        public Poll createFromParcel(Parcel in) {
            return new Poll(in);
        }

        @Override
        public Poll[] newArray(int size) {
            return new Poll[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(options);
        parcel.writeString(pollEndsDateTime);
    }
}
