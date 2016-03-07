package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Ref implements Parcelable {
    String name;
    String toString;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return toString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.toString);
    }

    public Ref() {
    }

    protected Ref(Parcel in) {
        this.name = in.readString();
        this.toString = in.readString();
    }

    public static final Parcelable.Creator<Ref> CREATOR = new Parcelable.Creator<Ref>() {
        public Ref createFromParcel(Parcel source) {
            return new Ref(source);
        }

        public Ref[] newArray(int size) {
            return new Ref[size];
        }
    };
}
