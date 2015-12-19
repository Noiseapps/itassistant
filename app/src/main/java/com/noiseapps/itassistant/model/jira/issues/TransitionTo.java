package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tomasz on 28.09.15.
 */
public class TransitionTo implements Parcelable {

    public static final Parcelable.Creator<TransitionTo> CREATOR = new Parcelable.Creator<TransitionTo>() {
        public TransitionTo createFromParcel(Parcel source) {
            return new TransitionTo(source);
        }

        public TransitionTo[] newArray(int size) {
            return new TransitionTo[size];
        }
    };
    @SerializedName("self")
    @Expose
    public String self;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("iconUrl")
    @Expose
    public String iconUrl;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public String id;

    public TransitionTo() {
    }

    public TransitionTo(String self, String description, String iconUrl, String name, String id) {
        this.self = self;
        this.description = description;
        this.iconUrl = iconUrl;
        this.name = name;
        this.id = id;
    }

    protected TransitionTo(Parcel in) {
        this.self = in.readString();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    @Override
    public String toString() {
        return "TransitionTo{" +
                "self='" + self + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionTo that = (TransitionTo) o;

        if (self != null ? !self.equals(that.self) : that.self != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (iconUrl != null ? !iconUrl.equals(that.iconUrl) : that.iconUrl != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.description);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }
}
