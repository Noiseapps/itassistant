package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.stash.commits.AuthorMetadata;
import com.noiseapps.itassistant.model.stash.general.StashUser;

public class PullRequestMember implements Parcelable {
    StashUser user;
    String role;
    boolean approved;

    @Override
    public String toString() {
        return "PullRequestMember{" +
                "user=" + user +
                ", role='" + role + '\'' +
                ", approved=" + approved +
                '}';
    }

    public StashUser getUser() {
        return user;
    }

    public void setUser(StashUser user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public PullRequestMember() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.role);
        dest.writeByte(approved ? (byte) 1 : (byte) 0);
    }

    protected PullRequestMember(Parcel in) {
        this.user = in.readParcelable(StashUser.class.getClassLoader());
        this.role = in.readString();
        this.approved = in.readByte() != 0;
    }

    public static final Creator<PullRequestMember> CREATOR = new Creator<PullRequestMember>() {
        public PullRequestMember createFromParcel(Parcel source) {
            return new PullRequestMember(source);
        }

        public PullRequestMember[] newArray(int size) {
            return new PullRequestMember[size];
        }
    };
}
