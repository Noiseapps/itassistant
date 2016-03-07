package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Segment implements Parcelable {
    String type;
    List<Line> lines;
    boolean truncated;

    public String getType() {
        return type;
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isTruncated() {
        return truncated;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "type='" + type + '\'' +
                ", lines=" + lines +
                ", truncated=" + truncated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeTypedList(lines);
        dest.writeByte(truncated ? (byte) 1 : (byte) 0);
    }

    public Segment() {
    }

    protected Segment(Parcel in) {
        this.type = in.readString();
        this.lines = in.createTypedArrayList(Line.CREATOR);
        this.truncated = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Segment> CREATOR = new Parcelable.Creator<Segment>() {
        public Segment createFromParcel(Parcel source) {
            return new Segment(source);
        }

        public Segment[] newArray(int size) {
            return new Segment[size];
        }
    };
}
