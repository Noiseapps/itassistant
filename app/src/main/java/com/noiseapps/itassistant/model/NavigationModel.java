package com.noiseapps.itassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

public class NavigationModel implements Parcelable {
    private final BaseAccount baseAccount;
    private final List<JiraProject> jiraProjects;

    public NavigationModel(BaseAccount baseAccount, List<JiraProject> jiraProjects) {
        this.baseAccount = baseAccount;
        this.jiraProjects = jiraProjects;
    }

    public List<JiraProject> getJiraProjects() {
        return jiraProjects;
    }

    public BaseAccount getBaseAccount() {
        return baseAccount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.baseAccount, 0);
        dest.writeTypedList(jiraProjects);
    }

    protected NavigationModel(Parcel in) {
        this.baseAccount = in.readParcelable(BaseAccount.class.getClassLoader());
        this.jiraProjects = in.createTypedArrayList(JiraProject.CREATOR);
    }

    public static final Parcelable.Creator<NavigationModel> CREATOR = new Parcelable.Creator<NavigationModel>() {
        public NavigationModel createFromParcel(Parcel source) {
            return new NavigationModel(source);
        }

        public NavigationModel[] newArray(int size) {
            return new NavigationModel[size];
        }
    };
}
