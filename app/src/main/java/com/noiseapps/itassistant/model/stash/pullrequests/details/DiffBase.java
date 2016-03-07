package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DiffBase implements Parcelable {
    public static final Parcelable.Creator<DiffBase> CREATOR = new Parcelable.Creator<DiffBase>() {
        public DiffBase createFromParcel(Parcel source) {
            return new DiffBase(source);
        }

        public DiffBase[] newArray(int size) {
            return new DiffBase[size];
        }
    };
    String fromHash;
    String toHash;
    int contextLines;
    String whitespace;
    List<Diff> diffs;

    public DiffBase() {
    }

    protected DiffBase(Parcel in) {
        this.fromHash = in.readString();
        this.toHash = in.readString();
        this.contextLines = in.readInt();
        this.whitespace = in.readString();
        this.diffs = in.createTypedArrayList(Diff.CREATOR);
    }

    public String getFromHash() {
        return fromHash;
    }

    public String getToHash() {
        return toHash;
    }

    public int getContextLines() {
        return contextLines;
    }

    public String getWhitespace() {
        return whitespace;
    }

    public List<Diff> getDiffs() {
        return diffs;
    }

    @Override
    public String toString() {
        return "DiffBase{" +
                "fromHash='" + fromHash + '\'' +
                ", toHash='" + toHash + '\'' +
                ", contextLines=" + contextLines +
                ", whitespace='" + whitespace + '\'' +
                ", diffs=" + diffs +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fromHash);
        dest.writeString(this.toHash);
        dest.writeInt(this.contextLines);
        dest.writeString(this.whitespace);
        dest.writeTypedList(diffs);
    }
}
