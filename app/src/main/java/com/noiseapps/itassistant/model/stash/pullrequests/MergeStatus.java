package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MergeStatus implements Parcelable {
    public static final Parcelable.Creator<MergeStatus> CREATOR = new Parcelable.Creator<MergeStatus>() {
        public MergeStatus createFromParcel(Parcel source) {
            return new MergeStatus(source);
        }

        public MergeStatus[] newArray(int size) {
            return new MergeStatus[size];
        }
    };
    boolean canMerge;
    boolean conflicted;
    ArrayList<Veto> vetoes;

    protected MergeStatus(Parcel in) {
        this.canMerge = in.readByte() != 0;
        this.conflicted = in.readByte() != 0;
        this.vetoes = in.createTypedArrayList(Veto.CREATOR);
    }

    public boolean isCanMerge() {
        return canMerge;
    }

    public boolean isConflicted() {
        return conflicted;
    }

    public List<Veto> getVetoes() {
        return vetoes;
    }

    @Override
    public String toString() {
        return "MergeStatus{" +
                "canMerge=" + canMerge +
                ", conflicted=" + conflicted +
                ", vetoes=" + vetoes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(canMerge ? (byte) 1 : (byte) 0);
        dest.writeByte(conflicted ? (byte) 1 : (byte) 0);
        dest.writeTypedList(vetoes);
    }
}
