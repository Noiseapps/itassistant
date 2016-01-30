package com.noiseapps.itassistant.model.stash.general;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StashRepoMeta implements Parcelable {
    public static final Creator<StashRepoMeta> CREATOR = new Creator<StashRepoMeta>() {
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
    private String scmId;
    private String state;
    private String statusMessage;
    private boolean forkable;
    @SerializedName("public")
    private boolean publicRepo;
    private String cloneUrl;
    private Links links;

    public StashRepoMeta() {
    }

    protected StashRepoMeta(Parcel in) {
        this.id = in.readInt();
        this.slug = in.readString();
        this.name = in.readString();
        this.scmId = in.readString();
        this.state = in.readString();
        this.statusMessage = in.readString();
        this.forkable = in.readByte() != 0;
        this.publicRepo = in.readByte() != 0;
        this.cloneUrl = in.readString();
        this.links = in.readParcelable(Links.class.getClassLoader());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StashRepoMeta that = (StashRepoMeta) o;

        if (id != that.id) return false;
        if (forkable != that.forkable) return false;
        if (publicRepo != that.publicRepo) return false;
        if (slug != null ? !slug.equals(that.slug) : that.slug != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (scmId != null ? !scmId.equals(that.scmId) : that.scmId != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (statusMessage != null ? !statusMessage.equals(that.statusMessage) : that.statusMessage != null)
            return false;
        if (cloneUrl != null ? !cloneUrl.equals(that.cloneUrl) : that.cloneUrl != null)
            return false;
        return links != null ? links.equals(that.links) : that.links == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (scmId != null ? scmId.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (statusMessage != null ? statusMessage.hashCode() : 0);
        result = 31 * result + (forkable ? 1 : 0);
        result = 31 * result + (publicRepo ? 1 : 0);
        result = 31 * result + (cloneUrl != null ? cloneUrl.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }

    public String getScmId() {

        return scmId;
    }

    public void setScmId(String scmId) {
        this.scmId = scmId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isForkable() {
        return forkable;
    }

    public void setForkable(boolean forkable) {
        this.forkable = forkable;
    }

    public boolean isPublicRepo() {
        return publicRepo;
    }

    public void setPublicRepo(boolean publicRepo) {
        this.publicRepo = publicRepo;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
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
        dest.writeString(this.scmId);
        dest.writeString(this.state);
        dest.writeString(this.statusMessage);
        dest.writeByte(forkable ? (byte) 1 : (byte) 0);
        dest.writeByte(publicRepo ? (byte) 1 : (byte) 0);
        dest.writeString(this.cloneUrl);
        dest.writeParcelable(this.links, 0);
    }
}
