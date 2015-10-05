package com.noiseapps.itassistant.model;

import java.util.List;

import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

public class NavigationModel {
    private final BaseAccount baseAccount;
    private final JiraUser user;
    private final List<JiraProject> jiraProjects;

    public NavigationModel(BaseAccount baseAccount, JiraUser user, List<JiraProject> jiraProjects) {
        this.baseAccount = baseAccount;
        this.user = user;
        this.jiraProjects = jiraProjects;
    }

    public JiraUser getUser() {
        return user;
    }

    public List<JiraProject> getJiraProjects() {
        return jiraProjects;
    }

    public BaseAccount getBaseAccount() {
        return baseAccount;
    }
}
