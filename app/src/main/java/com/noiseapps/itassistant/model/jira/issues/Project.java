package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Project implements Parcelable {

    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        public Project createFromParcel(Parcel source) {
            return new Project(source);
        }

        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;

    /**
     * No args constructor for use in serialization
     */
    public Project() {
    }

    /**
     * @param id
     * @param name
     * @param self
     * @param avatarUrls
     * @param key
     */
    public Project(String self, String id, String key, String name, AvatarUrls avatarUrls) {
        this.self = self;
        this.id = id;
        this.key = key;
        this.name = name;
        this.avatarUrls = avatarUrls;
    }

    protected Project(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
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
     * @return The key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key The key
     */
    public void setKey(String key) {
        this.key = key;
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
     * @return The avatarUrls
     */
    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    /**
     * @param avatarUrls The avatarUrls
     */
    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(id).append(key).append(name).append(avatarUrls).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Project) == false) {
            return false;
        }
        Project rhs = ((Project) other);
        return new EqualsBuilder().append(self, rhs.self).append(id, rhs.id).append(key, rhs.key).append(name, rhs.name).append(avatarUrls, rhs.avatarUrls).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeParcelable(this.avatarUrls, 0);
    }
}
