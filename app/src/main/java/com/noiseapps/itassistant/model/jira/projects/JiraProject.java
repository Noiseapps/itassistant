package com.noiseapps.itassistant.model.jira.projects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JiraProject implements Parcelable {

    public static final Parcelable.Creator<JiraProject> CREATOR = new Parcelable.Creator<JiraProject>() {
        public JiraProject createFromParcel(Parcel source) {
            return new JiraProject(source);
        }

        public JiraProject[] newArray(int size) {
            return new JiraProject[size];
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
    @SerializedName("projectCategory")
    @Expose
    private ProjectCategory projectCategory;

    public JiraProject() {
    }

    public JiraProject(String self, String id, String key, String name, AvatarUrls avatarUrls, ProjectCategory projectCategory) {
        this.self = self;
        this.id = id;
        this.key = key;
        this.name = name;
        this.avatarUrls = avatarUrls;
        this.projectCategory = projectCategory;
    }

    protected JiraProject(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
        this.projectCategory = in.readParcelable(ProjectCategory.class.getClassLoader());
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(ProjectCategory projectCategory) {
        this.projectCategory = projectCategory;
    }

    @Override
    public String toString() {
        return "JiraProject{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrls=" + avatarUrls +
                ", projectCategory=" + projectCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraProject that = (JiraProject) o;

        if (self != null ? !self.equals(that.self) : that.self != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (avatarUrls != null ? !avatarUrls.equals(that.avatarUrls) : that.avatarUrls != null)
            return false;
        return !(projectCategory != null ? !projectCategory.equals(that.projectCategory) : that.projectCategory != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (avatarUrls != null ? avatarUrls.hashCode() : 0);
        result = 31 * result + (projectCategory != null ? projectCategory.hashCode() : 0);
        return result;
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
        dest.writeParcelable(this.projectCategory, 0);
    }
}
