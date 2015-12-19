package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Votes implements Parcelable {

    public static final Parcelable.Creator<Votes> CREATOR = new Parcelable.Creator<Votes>() {
        public Votes createFromParcel(Parcel source) {
            return new Votes(source);
        }

        public Votes[] newArray(int size) {
            return new Votes[size];
        }
    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("votes")
    @Expose
    private long votes;
    @SerializedName("hasVoted")
    @Expose
    private boolean hasVoted;

    /**
     * No args constructor for use in serialization
     */
    public Votes() {
    }

    /**
     * @param hasVoted
     * @param votes
     * @param self
     */
    public Votes(String self, long votes, boolean hasVoted) {
        this.self = self;
        this.votes = votes;
        this.hasVoted = hasVoted;
    }

    protected Votes(Parcel in) {
        this.self = in.readString();
        this.votes = in.readLong();
        this.hasVoted = in.readByte() != 0;
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
     * @return The votes
     */
    public long getVotes() {
        return votes;
    }

    /**
     * @param votes The votes
     */
    public void setVotes(long votes) {
        this.votes = votes;
    }

    /**
     * @return The hasVoted
     */
    public boolean isHasVoted() {
        return hasVoted;
    }

    /**
     * @param hasVoted The hasVoted
     */
    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(votes).append(hasVoted).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Votes) == false) {
            return false;
        }
        Votes rhs = ((Votes) other);
        return new EqualsBuilder().append(self, rhs.self).append(votes, rhs.votes).append(hasVoted, rhs.hasVoted).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeLong(this.votes);
        dest.writeByte(hasVoted ? (byte) 1 : (byte) 0);
    }
}
