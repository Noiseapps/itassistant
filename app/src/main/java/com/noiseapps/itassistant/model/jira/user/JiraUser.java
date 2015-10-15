package com.noiseapps.itassistant.model.jira.user;

import android.os.Parcel;
import android.os.Parcelable;

public class JiraUser implements Parcelable {
    private final String name;
    private final String expand;
    private final String active;
    private final String emailAddress;
    private final String timeZone;
    private final String self;
    private final String displayName;
    private final AvatarUrls avatarUrls;
    private final Groups groups;

    public JiraUser(String name, String expand, String active, String emailAddress, String timeZone, String self, String displayName, AvatarUrls avatarUrls, Groups groups) {
        this.name = name;
        this.expand = expand;
        this.active = active;
        this.emailAddress = emailAddress;
        this.timeZone = timeZone;
        this.self = self;
        this.displayName = displayName;
        this.avatarUrls = avatarUrls;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public String getExpand() {
        return expand;
    }

    public String getActive() {
        return active;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getSelf() {
        return self;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public Groups getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraUser jiraUser = (JiraUser) o;

        if (name != null ? !name.equals(jiraUser.name) : jiraUser.name != null) return false;
        if (expand != null ? !expand.equals(jiraUser.expand) : jiraUser.expand != null)
            return false;
        if (active != null ? !active.equals(jiraUser.active) : jiraUser.active != null)
            return false;
        if (emailAddress != null ? !emailAddress.equals(jiraUser.emailAddress) : jiraUser.emailAddress != null)
            return false;
        if (timeZone != null ? !timeZone.equals(jiraUser.timeZone) : jiraUser.timeZone != null)
            return false;
        if (self != null ? !self.equals(jiraUser.self) : jiraUser.self != null) return false;
        if (displayName != null ? !displayName.equals(jiraUser.displayName) : jiraUser.displayName != null)
            return false;
        if (avatarUrls != null ? !avatarUrls.equals(jiraUser.avatarUrls) : jiraUser.avatarUrls != null)
            return false;
        return !(groups != null ? !groups.equals(jiraUser.groups) : jiraUser.groups != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (expand != null ? expand.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (self != null ? self.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (avatarUrls != null ? avatarUrls.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JiraUser{" +
                "name='" + name + '\'' +
                ", expand='" + expand + '\'' +
                ", active='" + active + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", self='" + self + '\'' +
                ", displayName='" + displayName + '\'' +
                ", avatarUrls=" + avatarUrls +
                ", groups=" + groups +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.expand);
        dest.writeString(this.active);
        dest.writeString(this.emailAddress);
        dest.writeString(this.timeZone);
        dest.writeString(this.self);
        dest.writeString(this.displayName);
        dest.writeParcelable(this.avatarUrls, 0);
        dest.writeParcelable(this.groups, 0);
    }

    protected JiraUser(Parcel in) {
        this.name = in.readString();
        this.expand = in.readString();
        this.active = in.readString();
        this.emailAddress = in.readString();
        this.timeZone = in.readString();
        this.self = in.readString();
        this.displayName = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.groups = in.readParcelable(Groups.class.getClassLoader());
    }

    public static final Parcelable.Creator<JiraUser> CREATOR = new Parcelable.Creator<JiraUser>() {
        public JiraUser createFromParcel(Parcel source) {
            return new JiraUser(source);
        }

        public JiraUser[] newArray(int size) {
            return new JiraUser[size];
        }
    };
}
