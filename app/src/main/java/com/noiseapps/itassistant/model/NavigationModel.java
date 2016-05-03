package com.noiseapps.itassistant.model;

import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;

public class NavigationModel {
    private final BaseAccount baseAccount;
    private final AbstractBaseProject[] jiraProjects;

    public NavigationModel(BaseAccount baseAccount, AbstractBaseProject[] jiraProjects) {
        this.baseAccount = baseAccount;
        this.jiraProjects = jiraProjects;
    }

    public AbstractBaseProject[] getJiraProjects() {
        return jiraProjects;
    }

    public BaseAccount getBaseAccount() {
        return baseAccount;
    }


}
