package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Progress implements Parcelable {

    public static final Parcelable.Creator<Progress> CREATOR = new Parcelable.Creator<Progress>() {
        public Progress createFromParcel(Parcel source) {
            return new Progress(source);
        }

        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };
    @SerializedName("progress")
    @Expose
    private long progress;
    @SerializedName("total")
    @Expose
    private long total;
    @SerializedName("percent")
    @Expose
    private long percent;

    /**
     * No args constructor for use in serialization
     */
    public Progress() {
    }

    /**
     * @param total
     * @param progress
     * @param percent
     */
    public Progress(long progress, long total, long percent) {
        this.progress = progress;
        this.total = total;
        this.percent = percent;
    }

    protected Progress(Parcel in) {
        this.progress = in.readLong();
        this.total = in.readLong();
        this.percent = in.readLong();
    }

    /**
     * @return The progress
     */
    public long getProgress() {
        return progress;
    }

    /**
     * @param progress The progress
     */
    public void setProgress(long progress) {
        this.progress = progress;
    }

    /**
     * @return The total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return The percent
     */
    public long getPercent() {
        return percent;
    }

    /**
     * @param percent The percent
     */
    public void setPercent(long percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(progress).append(total).append(percent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Progress) == false) {
            return false;
        }
        Progress rhs = ((Progress) other);
        return new EqualsBuilder().append(progress, rhs.progress).append(total, rhs.total).append(percent, rhs.percent).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.progress);
        dest.writeLong(this.total);
        dest.writeLong(this.percent);
    }
}
