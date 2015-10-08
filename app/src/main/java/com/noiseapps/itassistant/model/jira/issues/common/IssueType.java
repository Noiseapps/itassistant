package com.noiseapps.itassistant.model.jira.issues.common;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class IssueType implements Parcelable {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("statuses")
    @Expose
    private List<IssueStatus> issueStatuses;
    @SerializedName("subtask")
    @Expose
    private boolean subtask;

    @Override
    public String toString() {
        return "IssueType{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", issueStatuses=" + issueStatuses +
                ", subtask=" + subtask +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssueType issueType = (IssueType) o;

        if (subtask != issueType.subtask) return false;
        if (self != null ? !self.equals(issueType.self) : issueType.self != null) return false;
        if (id != null ? !id.equals(issueType.id) : issueType.id != null) return false;
        if (description != null ? !description.equals(issueType.description) : issueType.description != null)
            return false;
        if (iconUrl != null ? !iconUrl.equals(issueType.iconUrl) : issueType.iconUrl != null)
            return false;
        if (name != null ? !name.equals(issueType.name) : issueType.name != null) return false;
        return !(issueStatuses != null ? !issueStatuses.equals(issueType.issueStatuses) : issueType.issueStatuses != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (issueStatuses != null ? issueStatuses.hashCode() : 0);
        result = 31 * result + (subtask ? 1 : 0);
        return result;
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

    public List<IssueStatus> getIssueStatuses() {
        return issueStatuses;
    }

    public void setIssueStatuses(List<IssueStatus> issueStatuses) {
        this.issueStatuses = issueStatuses;
    }

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
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
        dest.writeString(this.iconUrl);
        dest.writeString(this.name);
        dest.writeTypedList(issueStatuses);
        dest.writeByte(subtask ? (byte) 1 : (byte) 0);
    }

    public IssueType() {
    }

    protected IssueType(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.description = in.readString();
        this.iconUrl = in.readString();
        this.name = in.readString();
        this.issueStatuses = in.createTypedArrayList(IssueStatus.CREATOR);
        this.subtask = in.readByte() != 0;
    }

    public static final Creator<IssueType> CREATOR = new Creator<IssueType>() {
        public IssueType createFromParcel(Parcel source) {
            return new IssueType(source);
        }

        public IssueType[] newArray(int size) {
            return new IssueType[size];
        }
    };
}
