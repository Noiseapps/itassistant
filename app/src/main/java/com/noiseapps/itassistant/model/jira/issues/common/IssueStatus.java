package com.noiseapps.itassistant.model.jira.issues.common;

import android.os.Parcel;
import android.os.Parcelable;

public class IssueStatus implements Parcelable {

    public static final Parcelable.Creator<IssueStatus> CREATOR = new Parcelable.Creator<IssueStatus>() {
        public IssueStatus createFromParcel(Parcel source) {
            return new IssueStatus(source);
        }

        public IssueStatus[] newArray(int size) {
            return new IssueStatus[size];
        }
    };
    private String id;
    private String name;
    private String iconUrl;
    private String description;
    private String self;

    public IssueStatus() {
    }

    protected IssueStatus(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.iconUrl = in.readString();
        this.description = in.readString();
        this.self = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueStatus that = (IssueStatus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return !(self != null ? !self.equals(that.self) : that.self != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (self != null ? self.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IssueStatus{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", description='" + description + '\'' +
                ", self='" + self + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.iconUrl);
        dest.writeString(this.description);
        dest.writeString(this.self);
    }
}
