package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

public class Line implements Parcelable {
    int destination;
    int source;
    String line;
    boolean truncated;

    @Override
    public String toString() {
        return "Line{" +
                "destination=" + destination +
                ", source=" + source +
                ", line='" + line + '\'' +
                ", truncated=" + truncated +
                '}';
    }

    public int getDestination() {
        return destination;
    }

    public int getSource() {
        return source;
    }

    public String getLine() {
        return line;
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
        dest.writeInt(this.destination);
        dest.writeInt(this.source);
        dest.writeString(this.line);
        dest.writeByte(truncated ? (byte) 1 : (byte) 0);
    }

    public Line() {
    }

    protected Line(Parcel in) {
        this.destination = in.readInt();
        this.source = in.readInt();
        this.line = in.readString();
        this.truncated = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Line> CREATOR = new Parcelable.Creator<Line>() {
        public Line createFromParcel(Parcel source) {
            return new Line(source);
        }

        public Line[] newArray(int size) {
            return new Line[size];
        }
    };
}
