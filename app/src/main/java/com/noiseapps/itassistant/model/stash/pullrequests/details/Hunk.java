package com.noiseapps.itassistant.model.stash.pullrequests.details;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Hunk implements Parcelable {

    int sourceLine;
    int sourceSpan;
    int destinationLine;
    int destinationSpan;
    List<Segment> segments;
    boolean truncated;

    @Override
    public String toString() {
        return "Hunk{" +
                "sourceLine=" + sourceLine +
                ", sourceSpan=" + sourceSpan +
                ", destinationLine=" + destinationLine +
                ", destinationSpan=" + destinationSpan +
                ", segments=" + segments +
                ", truncated=" + truncated +
                '}';
    }

    public int getSourceLine() {
        return sourceLine;
    }

    public int getSourceSpan() {
        return sourceSpan;
    }

    public int getDestinationLine() {
        return destinationLine;
    }

    public int getDestinationSpan() {
        return destinationSpan;
    }

    public List<Segment> getSegments() {
        return segments;
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
        dest.writeInt(this.sourceLine);
        dest.writeInt(this.sourceSpan);
        dest.writeInt(this.destinationLine);
        dest.writeInt(this.destinationSpan);
        dest.writeTypedList(segments);
        dest.writeByte(truncated ? (byte) 1 : (byte) 0);
    }

    public Hunk() {
    }

    protected Hunk(Parcel in) {
        this.sourceLine = in.readInt();
        this.sourceSpan = in.readInt();
        this.destinationLine = in.readInt();
        this.destinationSpan = in.readInt();
        this.segments = in.createTypedArrayList(Segment.CREATOR);
        this.truncated = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Hunk> CREATOR = new Parcelable.Creator<Hunk>() {
        public Hunk createFromParcel(Parcel source) {
            return new Hunk(source);
        }

        public Hunk[] newArray(int size) {
            return new Hunk[size];
        }
    };
}
