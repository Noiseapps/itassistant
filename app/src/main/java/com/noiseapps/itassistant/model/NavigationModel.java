package com.noiseapps.itassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;

import java.util.List;

public class NavigationModel implements Parcelable {
    public static final Parcelable.Creator<NavigationModel> CREATOR = new Parcelable.Creator<NavigationModel>() {
        public NavigationModel createFromParcel(Parcel source) {
            return new NavigationModel(source);
        }

        public NavigationModel[] newArray(int size) {
            return new NavigationModel[size];
        }
    };
    private final BaseAccount baseAccount;
    private final List<JiraProject> jiraProjects;

    public NavigationModel(BaseAccount baseAccount, List<JiraProject> jiraProjects) {
        this.baseAccount = baseAccount;
        this.jiraProjects = jiraProjects;
    }

    protected NavigationModel(Parcel in) {
        this.baseAccount = in.readParcelable(BaseAccount.class.getClassLoader());
        this.jiraProjects = in.createTypedArrayList(JiraProject.CREATOR);
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
}
