package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

public class FixVersion implements Parcelable {

    public static final Parcelable.Creator<FixVersion> CREATOR = new Parcelable.Creator<FixVersion>() {
        public FixVersion createFromParcel(Parcel source) {
            return new FixVersion(source);
        }

        public FixVersion[] newArray(int size) {
            return new FixVersion[size];
        }
    };
    private String self;
    private String name;
    private String id;
    private String description;
    private boolean archived;
    private boolean released;
    private String releaseDate;

    public FixVersion() {
    }

    protected FixVersion(Parcel in) {
        this.self = in.readString();
        this.name = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.archived = in.readByte() != 0;
        this.released = in.readByte() != 0;
        this.releaseDate = in.readString();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FixVersion that = (FixVersion) o;

        if (archived != that.archived) return false;
        if (released != that.released) return false;
        if (self != null ? !self.equals(that.self) : that.self != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return !(releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (archived ? 1 : 0);
        result = 31 * result + (released ? 1 : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        return result;
    }

    public String getSelf() {

        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeByte(archived ? (byte) 1 : (byte) 0);
        dest.writeByte(released ? (byte) 1 : (byte) 0);
        dest.writeString(this.releaseDate);
    }
}
