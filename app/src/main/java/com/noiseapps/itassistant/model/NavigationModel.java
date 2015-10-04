package com.noiseapps.itassistant.model;

import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

import java.util.List;

public class NavigationModel {
    private final JiraUser user;
    private final List<JiraProject> jiraProjects;

    public NavigationModel(JiraUser user, List<JiraProject> jiraProjects) {
        this.user = user;
        this.jiraProjects = jiraProjects;
    }

    public JiraUser getUser() {
        return user;
    }

    public List<JiraProject> getJiraProjects() {
        return jiraProjects;
    }
}
