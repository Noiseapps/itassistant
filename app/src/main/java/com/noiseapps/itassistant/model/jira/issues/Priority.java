
package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Priority implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("statusColor")
    @Expose
    private String statusColor;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;


    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
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
    public String toString() {
        return "Priority{" +
                "self='" + self + '\'' +
                ", statusColor='" + statusColor + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Priority priority = (Priority) o;

        if (self != null ? !self.equals(priority.self) : priority.self != null) return false;
        if (statusColor != null ? !statusColor.equals(priority.statusColor) : priority.statusColor != null)
            return false;
        if (iconUrl != null ? !iconUrl.equals(priority.iconUrl) : priority.iconUrl != null)
            return false;
        if (name != null ? !name.equals(priority.name) : priority.name != null) return false;
        return !(id != null ? !id.equals(priority.id) : priority.id != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (statusColor != null ? statusColor.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.statusColor);
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public Priority() {
    }

    protected Priority(Parcel in) {
        this.self = in.readString();
        this.statusColor = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Creator<Priority> CREATOR = new Creator<Priority>() {
        public Priority createFromParcel(Parcel source) {
            return new Priority(source);
        }

        public Priority[] newArray(int size) {
            return new Priority[size];
        }
    };
}
