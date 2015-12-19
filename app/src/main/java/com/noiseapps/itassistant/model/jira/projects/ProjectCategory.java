package com.noiseapps.itassistant.model.jira.projects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProjectCategory implements Parcelable {

    public static final Parcelable.Creator<ProjectCategory> CREATOR = new Parcelable.Creator<ProjectCategory>() {
        public ProjectCategory createFromParcel(Parcel source) {
            return new ProjectCategory(source);
        }

        public ProjectCategory[] newArray(int size) {
            return new ProjectCategory[size];
        }
    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * No args constructor for use in serialization
     */
    public ProjectCategory() {
    }

    /**
     * @param id
     * @param description
     * @param name
     * @param self
     */
    public ProjectCategory(String self, String id, String name, String description) {
        this.self = self;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    protected ProjectCategory(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
    }

    /**
     * @return The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * @param self The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(id).append(name).append(description).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProjectCategory) == false) {
            return false;
        }
        ProjectCategory rhs = ((ProjectCategory) other);
        return new EqualsBuilder().append(self, rhs.self).append(id, rhs.id).append(name, rhs.name).append(description, rhs.description).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
    }
}
