package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class PullRequestAttributes implements Parcelable {

    String[] commentCount;

    public String[] getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String[] commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "PullRequestAttributes{" +
                "commentCount=" + Arrays.toString(commentCount) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(this.commentCount);
    }

    public PullRequestAttributes() {
    }

    protected PullRequestAttributes(Parcel in) {
        this.commentCount = in.createStringArray();
    }

    public static final Parcelable.Creator<PullRequestAttributes> CREATOR = new Parcelable.Creator<PullRequestAttributes>() {
        public PullRequestAttributes createFromParcel(Parcel source) {
            return new PullRequestAttributes(source);
        }

        public PullRequestAttributes[] newArray(int size) {
            return new PullRequestAttributes[size];
        }
    };
}
