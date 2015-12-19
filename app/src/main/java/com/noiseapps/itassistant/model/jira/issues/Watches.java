package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Watches implements Parcelable {

    public static final Parcelable.Creator<Watches> CREATOR = new Parcelable.Creator<Watches>() {
        public Watches createFromParcel(Parcel source) {
            return new Watches(source);
        }

        public Watches[] newArray(int size) {
            return new Watches[size];
        }
    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("watchCount")
    @Expose
    private long watchCount;
    @SerializedName("isWatching")
    @Expose
    private boolean isWatching;

    /**
     * No args constructor for use in serialization
     */
    public Watches() {
    }

    /**
     * @param watchCount
     * @param isWatching
     * @param self
     */
    public Watches(String self, long watchCount, boolean isWatching) {
        this.self = self;
        this.watchCount = watchCount;
        this.isWatching = isWatching;
    }

    protected Watches(Parcel in) {
        this.self = in.readString();
        this.watchCount = in.readLong();
        this.isWatching = in.readByte() != 0;
    }

    /**
     * @return The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * @param self The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * @return The watchCount
     */
    public long getWatchCount() {
        return watchCount;
    }

    /**
     * @param watchCount The watchCount
     */
    public void setWatchCount(long watchCount) {
        this.watchCount = watchCount;
    }

    /**
     * @return The isWatching
     */
    public boolean isIsWatching() {
        return isWatching;
    }

    /**
     * @param isWatching The isWatching
     */
    public void setIsWatching(boolean isWatching) {
        this.isWatching = isWatching;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(watchCount).append(isWatching).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Watches) == false) {
            return false;
        }
        Watches rhs = ((Watches) other);
        return new EqualsBuilder().append(self, rhs.self).append(watchCount, rhs.watchCount).append(isWatching, rhs.isWatching).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeLong(this.watchCount);
        dest.writeByte(isWatching ? (byte) 1 : (byte) 0);
    }
}
