
package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Reporter implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("active")
    @Expose
    private boolean active;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Reporter() {
    }

    /**
     * 
     * @param name
     * @param active
     * @param emailAddress
     * @param self
     * @param displayName
     * @param avatarUrls
     */
    public Reporter(String self, String name, String emailAddress, AvatarUrls avatarUrls, String displayName, boolean active) {
        this.self = self;
        this.name = name;
        this.emailAddress = emailAddress;
        this.avatarUrls = avatarUrls;
        this.displayName = displayName;
        this.active = active;
    }

    /**
     * 
     * @return
     *     The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 
     * @param emailAddress
     *     The emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 
     * @return
     *     The avatarUrls
     */
    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    /**
     * 
     * @param avatarUrls
     *     The avatarUrls
     */
    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(name).append(emailAddress).append(avatarUrls).append(displayName).append(active).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Reporter) == false) {
            return false;
        }
        Reporter rhs = ((Reporter) other);
        return new EqualsBuilder().append(self, rhs.self).append(name, rhs.name).append(emailAddress, rhs.emailAddress).append(avatarUrls, rhs.avatarUrls).append(displayName, rhs.displayName).append(active, rhs.active).isEquals();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.name);
        dest.writeString(this.emailAddress);
        dest.writeParcelable(this.avatarUrls, 0);
        dest.writeString(this.displayName);
        dest.writeByte(active ? (byte) 1 : (byte) 0);
    }

    protected Reporter(Parcel in) {
        this.self = in.readString();
        this.name = in.readString();
        this.emailAddress = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.displayName = in.readString();
        this.active = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Reporter> CREATOR = new Parcelable.Creator<Reporter>() {
        public Reporter createFromParcel(Parcel source) {
            return new Reporter(source);
        }

        public Reporter[] newArray(int size) {
            return new Reporter[size];
        }
    };
}
