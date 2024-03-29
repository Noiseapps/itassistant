package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Resolution implements Parcelable {

    public static final Parcelable.Creator<Resolution> CREATOR = new Parcelable.Creator<Resolution>() {
        public Resolution createFromParcel(Parcel source) {
            return new Resolution(source);
        }

        public Resolution[] newArray(int size) {
            return new Resolution[size];
        }
    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * No args constructor for use in serialization
     */
    public Resolution() {
    }

    /**
     * @param id
     * @param description
     * @param name
     * @param self
     */
    public Resolution(String self, String id, String description, String name) {
        this.self = self;
        this.id = id;
        this.description = description;
        this.name = name;
    }

    protected Resolution(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.name = in.readString();
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(id).append(description).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Resolution) == false) {
            return false;
        }
        Resolution rhs = ((Resolution) other);
        return new EqualsBuilder().append(self, rhs.self).append(id, rhs.id).append(description, rhs.description).append(name, rhs.name).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.name);
    }
}
