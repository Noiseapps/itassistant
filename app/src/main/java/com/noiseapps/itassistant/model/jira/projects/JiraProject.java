package com.noiseapps.itassistant.model.jira.projects;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;

public class JiraProject extends AbstractBaseProject implements Parcelable {

    public static final Creator<JiraProject> CREATOR = new Creator<JiraProject>() {
        public JiraProject createFromParcel(Parcel source) {
            return new JiraProject(source);
        }

        public JiraProject[] newArray(int size) {
            return new JiraProject[size];
        }
    };
    private AvatarUrls avatarUrls;


    public JiraProject() {
    }


    protected JiraProject(Parcel in) {
        super(in);
        this.avatarUrls = in.readParcelable(AvatarUrls.class.getClassLoader());
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    @Override
    public int getAccountType() {
        return AccountTypes.ACC_JIRA;
    }

    @Override
    public String toString() {
        return "JiraProject{" +
                "avatarUrls=" + avatarUrls +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JiraProject that = (JiraProject) o;

        return !(avatarUrls != null ? !avatarUrls.equals(that.avatarUrls) : that.avatarUrls != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (avatarUrls != null ? avatarUrls.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.avatarUrls, 0);
    }
}
