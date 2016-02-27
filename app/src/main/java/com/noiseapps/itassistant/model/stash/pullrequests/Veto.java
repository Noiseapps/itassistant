package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

public class Veto implements Parcelable {

    public static final Parcelable.Creator<Veto> CREATOR = new Parcelable.Creator<Veto>() {
        public Veto createFromParcel(Parcel source) {
            return new Veto(source);
        }

        public Veto[] newArray(int size) {
            return new Veto[size];
        }
    };
    private String summaryMessage;
    private String detailedMessage;

    public Veto() {
    }

    protected Veto(Parcel in) {
        this.summaryMessage = in.readString();
        this.detailedMessage = in.readString();
    }

    @Override
    public String toString() {
        return "Veto{" +
                "summaryMessage='" + summaryMessage + '\'' +
                ", detailedMessage='" + detailedMessage + '\'' +
                '}';
    }

    public String getSummaryMessage() {
        return summaryMessage;
    }

    public void setSummaryMessage(String summaryMessage) {
        this.summaryMessage = summaryMessage;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summaryMessage);
        dest.writeString(this.detailedMessage);
    }
}
