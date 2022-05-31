package com.kitcod.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedsView implements Parcelable {

    @SerializedName("_embedded")
    @Expose
    public Embedded embedded;

    protected FeedsView(Parcel in) {
        embedded = in.readParcelable(Embedded.class.getClassLoader());
    }

    public static final Creator<FeedsView> CREATOR = new Creator<FeedsView>() {
        @Override
        public FeedsView createFromParcel(Parcel in) {
            return new FeedsView(in);
        }

        @Override
        public FeedsView[] newArray(int size) {
            return new FeedsView[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(embedded, i);
    }
 /*   @SerializedName("_links")
    @Expose
    public Links links;
    @SerializedName("page")
    @Expose
    public Page page;*/


}
