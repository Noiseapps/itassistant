package com.noiseapps.itassistant.model.stash.general;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.stash.pullrequests.Links;


public class StashUser implements Parcelable {
    int id;
    String name;
    String displayName;
    String emailAddress;
    String slug;
    Links links;

    public Links getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "StashUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", slug='" + slug + '\'' +
                ", links=" + links +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSlug() {
        return slug;
    }

    public String getAvatarUrl() {
        try {
            return getLinks().getSelf().get(0).getHref() + "/avatar.png";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.displayName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.slug);
        dest.writeParcelable(this.links, 0);
    }

    public StashUser() {
    }

    protected StashUser(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.displayName = in.readString();
        this.emailAddress = in.readString();
        this.slug = in.readString();
        this.links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Parcelable.Creator<StashUser> CREATOR = new Parcelable.Creator<StashUser>() {
        public StashUser createFromParcel(Parcel source) {
            return new StashUser(source);
        }

        public StashUser[] newArray(int size) {
            return new StashUser[size];
        }
    };
}
