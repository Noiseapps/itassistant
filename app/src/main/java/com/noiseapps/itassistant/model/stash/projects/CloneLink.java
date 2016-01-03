package com.noiseapps.itassistant.model.stash.projects;

import android.os.Parcel;
import android.os.Parcelable;

public class CloneLink implements Parcelable {
    private String href;
    private String name;

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
        dest.writeString(this.name);
    }

    public CloneLink() {
    }

    protected CloneLink(Parcel in) {
        this.href = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<CloneLink> CREATOR = new Parcelable.Creator<CloneLink>() {
        public CloneLink createFromParcel(Parcel source) {
            return new CloneLink(source);
        }

        public CloneLink[] newArray(int size) {
            return new CloneLink[size];
        }
    };
}
