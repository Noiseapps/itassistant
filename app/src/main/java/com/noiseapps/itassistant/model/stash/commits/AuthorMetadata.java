package com.noiseapps.itassistant.model.stash.commits;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthorMetadata implements Parcelable {
    private String name;
    private String emailAddress;
    private String displayName;
    private String avatarUrl;

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.emailAddress);
        dest.writeString(this.displayName);
        dest.writeString(this.avatarUrl);
    }

    public AuthorMetadata() {
    }

    protected AuthorMetadata(Parcel in) {
        this.name = in.readString();
        this.emailAddress = in.readString();
        this.displayName = in.readString();
        this.avatarUrl = in.readString();
    }

    public static final Parcelable.Creator<AuthorMetadata> CREATOR = new Parcelable.Creator<AuthorMetadata>() {
        public AuthorMetadata createFromParcel(Parcel source) {
            return new AuthorMetadata(source);
        }

        public AuthorMetadata[] newArray(int size) {
            return new AuthorMetadata[size];
        }
    };

    @Override
    public String toString() {
        return "AuthorMetadata{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
