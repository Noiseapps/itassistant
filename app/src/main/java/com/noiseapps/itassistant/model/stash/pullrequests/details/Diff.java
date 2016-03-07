package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Diff implements Parcelable {
    Ref source;
    Ref destination;
    List<Hunk> hunks;
    boolean truncated;

    @Override
    public String toString() {
        return "Diff{" +
                "source=" + source +
                ", destination=" + destination +
                ", hunks=" + hunks +
                ", truncated=" + truncated +
                '}';
    }

    public Ref getSource() {
        return source;
    }

    public Ref getDestination() {
        return destination;
    }

    public List<Hunk> getHunks() {
        return hunks;
    }

    public boolean isTruncated() {
        return truncated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.source, flags);
        dest.writeParcelable(this.destination, flags);
        dest.writeTypedList(hunks);
        dest.writeByte(truncated ? (byte) 1 : (byte) 0);
    }

    public Diff() {
    }

    protected Diff(Parcel in) {
        this.source = in.readParcelable(Ref.class.getClassLoader());
        this.destination = in.readParcelable(Ref.class.getClassLoader());
        this.hunks = in.createTypedArrayList(Hunk.CREATOR);
        this.truncated = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Diff> CREATOR = new Parcelable.Creator<Diff>() {
        public Diff createFromParcel(Parcel source) {
            return new Diff(source);
        }

        public Diff[] newArray(int size) {
            return new Diff[size];
        }
    };
}
