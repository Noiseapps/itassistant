package com.noiseapps.itassistant.model.stash.projects;

import android.os.Parcel;
import android.os.Parcelable;

public class StashRepoMeta implements Parcelable {
    public static final Parcelable.Creator<StashRepoMeta> CREATOR = new Parcelable.Creator<StashRepoMeta>() {
        public StashRepoMeta createFromParcel(Parcel source) {
            return new StashRepoMeta(source);
        }

        public StashRepoMeta[] newArray(int size) {
            return new StashRepoMeta[size];
        }
    };
    private int id;
    private String slug;
    private String name;

    public StashRepoMeta() {
    }

    protected StashRepoMeta(Parcel in) {
        this.id = in.readInt();
        this.slug = in.readString();
        this.name = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", slug, name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.slug);
        dest.writeString(this.name);
    }
}
