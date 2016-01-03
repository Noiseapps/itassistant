package com.noiseapps.itassistant.model.stash.projects;

import android.os.Parcel;
import android.os.Parcelable;

public class SelfLink implements Parcelable {
    private String url;
    private String self;

    public String getUrl() {
        return url;
    }

    public String getSelf() {
        return self;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.self);
    }

    public SelfLink() {
    }

    protected SelfLink(Parcel in) {
        this.url = in.readString();
        this.self = in.readString();
    }

    public static final Parcelable.Creator<SelfLink> CREATOR = new Parcelable.Creator<SelfLink>() {
        public SelfLink createFromParcel(Parcel source) {
            return new SelfLink(source);
        }

        public SelfLink[] newArray(int size) {
            return new SelfLink[size];
        }
    };
}
