package com.noiseapps.itassistant.model.stash.projects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Links implements Parcelable {

    public static final Creator<Links> CREATOR = new Creator<Links>() {
        public Links createFromParcel(Parcel source) {
            return new Links(source);
        }

        public Links[] newArray(int size) {
            return new Links[size];
        }
    };
    @SerializedName("clone")
    private List<CloneLink> cloneLinks;
    @SerializedName("self")
    private List<SelfLink> selfLinks;

    public Links() {
    }


    protected Links(Parcel in) {
        this.cloneLinks = in.createTypedArrayList(CloneLink.CREATOR);
        this.selfLinks = in.createTypedArrayList(SelfLink.CREATOR);
    }

    public List<CloneLink> getCloneLinks() {
        return cloneLinks;
    }

    public List<SelfLink> getSelfLinks() {
        return selfLinks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cloneLinks);
        dest.writeTypedList(selfLinks);
    }
}
