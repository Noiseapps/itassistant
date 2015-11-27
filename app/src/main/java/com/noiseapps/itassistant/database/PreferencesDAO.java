package com.noiseapps.itassistant.database;

import com.google.gson.Gson;
import com.noiseapps.itassistant.model.TimeTrackingInfo;
import com.noiseapps.itassistant.model.account.BaseAccount;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EBean
public class PreferencesDAO {

    @Pref
    Preferences_ preferences;

    private final Gson GSON = new Gson();

    public BaseAccount getJiraConfig() {
        final String configJson = preferences.jiraConfig().get();
        return GSON.fromJson(configJson, BaseAccount.class);
    }

    public void saveJiraConfig(BaseAccount newConfig) {
        final String configJson = GSON.toJson(newConfig);
        preferences.jiraConfig().put(configJson);
    }

    public TimeTrackingInfo getTimeTrackingInfo() {
        final String configJson = preferences.timeTracking().get();
        return GSON.fromJson(configJson, TimeTrackingInfo.class);
    }

    public void setTimeTrackingInfo(TimeTrackingInfo newConfig) {
        final String configJson = GSON.toJson(newConfig);
        preferences.timeTracking().put(configJson);
    }

}
