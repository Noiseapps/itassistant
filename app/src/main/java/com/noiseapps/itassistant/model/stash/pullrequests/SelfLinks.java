package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

public class SelfLinks implements Parcelable {
    String href;

    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        return "SelfLinks{" +
                "href='" + href + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public SelfLinks() {
    }

    protected SelfLinks(Parcel in) {
        this.href = in.readString();
    }

    public static final Parcelable.Creator<SelfLinks> CREATOR = new Parcelable.Creator<SelfLinks>() {
        public SelfLinks createFromParcel(Parcel source) {
            return new SelfLinks(source);
        }

        public SelfLinks[] newArray(int size) {
            return new SelfLinks[size];
        }
    };
}
