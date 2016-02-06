package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Links implements Parcelable {

    List<SelfLinks> self;

    public List<SelfLinks> getSelf() {
        return self;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(self);
    }

    public Links() {
    }

    protected Links(Parcel in) {
        this.self = in.createTypedArrayList(SelfLinks.CREATOR);
    }

    public static final Parcelable.Creator<Links> CREATOR = new Parcelable.Creator<Links>() {
        public Links createFromParcel(Parcel source) {
            return new Links(source);
        }

        public Links[] newArray(int size) {
            return new Links[size];
        }
    };
}
